# Initializer

## Setup
1. Build `:initialization:domain` and `initialization:ui` modules.
2. Add `.aar`-s created in step 1 to your project.
3. Add `mvi-compose` and `mvi-core` GFT libraries to your dependencies.  

## Usage

1. Define any number of initializers by implementing the `Initializer` interface.
   ```kotlin
   class SomeInitializer : Initializer {
       override suspend fun initialize() {
           // do some stuff
       }
   }
   ```
   > ⚠ `initialize` method will run on the current thread which may be the `main` thread. 
   > Use `withContext(dispatcher)` to perform long running operations.

2. Define the initializers graph.
   - By default initializers run in the same order as defined in the list.
   - If you want to run a few initializers in parallel use `initializeInParallel`.
   - Initializers are instantiated lazily.
   - If a few initializers run in parallel and one of them fails,
     the application will wait for all the remaining initializers in the set before propagating the exception.
     <br />However, if multiple initializers fail, only the error thrown by the failing initializer
     that is the highest on the list will be propagated.
   - If a few initializers run in a sequence and one of them fails the initialization is terminated immediately.
   ```kotlin
   val initializationGraph = listOf(
      { InitializerOne() },
      { InitializerTwo() },
      {
         initializeInParallel(
            { InitializerThree() },
            { InitializerFour() }
         )
      },
      { InitializerFive() }
   )   

   // or with Koin
   val ApplicationInitializationQualifier = named("ApplicationInitializationQualifier")
   factory(ApplicationInitializationQualifier) {
      listOf(
         { get<InitializerOne>() },
         { get<InitializerTwo>() },
         {
            initializeInParallel(
               { get<InitializerThree>() },
               { get<InitializerFour>() }
            )
         },
         { get<InitializerFive>() }
      )
   }
   ```
    
3. Define initialization process.
   - You may define as many initialization processes as you want, but each of them requires a separate `InitializationIdentifier`.
   - If you don't implement Clean Architecture or simply don't want to use `UseCases` you may use `InitializationService.defineInitializationProcess` directly.
   ```kotlin
   val applicationInitializationIdentifier = InitializationIdentifier("ApplicationInitializationIdentifier")
   val defineInitializationProcess: DefineInitializationProcessUseCase = ...
   
   defineInitializationProcess(
      identifier = ApplicationInitializationIdentifier,
      initializers = initializationGraph
   )
   
   // or with Koin
   defineInitializationProcessUseCase(
      identifier = ApplicationInitializationIdentifier,
      initializers = get(ApplicationInitializationQualifier)
   )
   ```

4. Define any number of error renderers.
   ```kotlin
   class VerySpecificErrorRenderer(
      private val restartApplication: RestartApplicationUseCase,
   ) : InitializationErrorRenderer {
      @Composable
      override fun RenderError(error: Throwable) {
         Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
         ) {
            Text(
               text = "Oh, no!\n Very specific error happened!",
               textAlign = TextAlign.Center,
               color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
               onClick = {
                  restartApplication()
               }
            ) {
               Text(text = "Try again") 
            } 
         }
      }
   
      override fun canRenderError(error: Throwable): Boolean = error is VerySpecificError
   }
   ```
   - You need to specify which errors are supported by the given `InitializationRenderer` with `fun canRenderError(error: Throwable)`.
   - `RenderError` is just a common `@Composable` method. We suggest following MVI pattern while implementing error renderers.
   > ℹ The presented error renderer simply restarts the whole application when user clicks "Try again" button.<br />
     However, one may retry just the initialization by calling `StartInitializationUseCase` again with the same identifier.
   
5. Define the order of error renderers by implementing `InitializationErrorRenderer` interface.<br />
   Everytime initialization fails, the error renders list is searched for an appropriate renderer -
   the first error renderer that returns `true` from `fun canRenderError(error: Throwable): Boolean` is chosen. 
   ```kotlin
   val applicationInitializationErrorRenderers = {
      listOf(
         VerySpecificErrorRenderer(),
         GeneralErrorRenderer()
      )
   }
   
   // or with Koin
   val ApplicationInitializationErrorRenderers = named("AppInitializationErrorRenderers")
   factory(qualifier = ApplicationInitializationErrorRenderers) {
      {
         listOf(
            get<VerySpecificErrorRenderer>(),
            get<GeneralErrorRenderer>()
         )
      }
   }
   ```
   > ℹ Note that initialization renderers list is created in a lazy fashion.
   
6. Show application content depending on the initialization status.
   ```kotlin
   Initialize(
      initializationIdentifier = ApplicationInitializationIdentifier,
      errorRenderersProvider = applicationInitializationErrorRenderers,
   ) {
      // application content
   }
   ```
   > ℹ Alternatively you may show you custom initialization progress renderer. 
   > Check the **Custom initialization progress indicator** section below for more details.

7. Start initialization process.<br />
   There are two ways of staring initialization process:
   - manual with `StartInitializationUseCase` (or `InitializationService.initialize`)
   - automatic - initialization starts as soon as `Initialize` composable method enters the Composition.

### Integration with the AndroidX SplashScreen library
If you want to keep the splash screen until the initialization is complete you need to use `setKeepOnScreenCondition`.
```kotlin
class MainActivity : ComponentActivity(), KoinComponent {
   override fun onCreate(savedInstanceState: Bundle?) {
       
      installSplashScreen().setKeepOnScreenCondition {
         val initState = get<StreamInitializationStateUseCase>()(ApplicationInitializationIdentifier).value
         initState != InitState.Initialized && initState !is InitState.Failed
      }
      
      super.onCreate(savedInstanceState)
      
      ...   
         Initialize(...) {
             
         }
      ...
   }
}
```
> ⚠ You still have to use `Initialize` composable in your composition to render initialization errors and/or start initialization automatically.

### Custom initialization progress indicator (e.g. Ccstom Splash Screen) 
Whenever you want to control on your own whether application content is displayed you may use `StreamInitializationStateUseCase`
to track the initialization progress.

You need to:
1. Create a composable which controls whether application content is visible.
   ```kotlin
   data class CustomSplashScreenViewState(
      internal val isLoadingIndicatorVisible: Boolean,
      internal val isContentVisible: Boolean,
   ) : ViewState
   ```
   ```kotlin
   @Composable
   fun CustomSplashScreen(
      initializationIdentifier: InitializationIdentifier,
      viewModel: MviViewModel<CustomSplashScreenViewState, ViewEvent, NavigationEffect, ViewEffect> = ...,
      content: @Composable () -> Unit,
   ) {
      ViewState(viewModel) {
         if (viewState.isContentVisible) {
            content()
         }
   
         if (viewState.isLoadingIndicatorVisible) {
            Dialog(onDismissRequest = { }) {
               Box(
                  modifier = Modifier.fillMaxSize()
               ) {
                  CircularProgressIndicator(
                     modifier = Modifier.align(Alignment.Center)
                  )
               }
            }
         }
      }
   }
   ```
   ```kotlin
   class CustomSplashScreenViewModel(
      initializationIdentifier: InitializationIdentifier,
      streamInitializationState: StreamInitializationStateUseCase,
   ) : BaseMviViewModel<CustomSplashScreenViewState, ViewEvent, NavigationEffect, ViewEffect>() {
   
      override val viewStates: StateFlow<CustomSplashScreenViewState> = streamInitializationState(initializationIdentifier)
         .map { state ->
            state.toViewState()
         }
         .toViewStates(
            streamInitializationState(initializationIdentifier).value.toViewState(),
            viewModelScope
         )
   
      override fun onEvent(event: ViewEvent) = Unit
   
      private fun InitState.toViewState() = CustomSplashScreenViewState(
         isLoadingIndicatorVisible = this != Initialized,
         isContentVisible = this == Initialized
      )
   }
   ```
2. Provide your progress indicator as `content` parameter of the `Initialize` method.
3. Set the `showContentDuringInitialization` parameter of `Initialize` composable to `true`
   ```kotlin
   Initialize(
      initializationIdentifier = ApplicationInitializationIdentifier,
      showContentDuringInitialization = true,
      errorRenderersProvider = get(AppInitializationErrorRenderers)
   ) {
   
      CustomSplashScreen(
         initializationIdentifier = ApplicationInitializationIdentifier
      ) {
         ApplicationContent(
            modifier = Modifier.padding(innerPadding)
         )
      }
   }
   ```
