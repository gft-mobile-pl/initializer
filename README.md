# Initializer

## Setup
1. Build `:initialization:domain` and `initialization:ui` modules.
2. Add `.aar`-s created in step 1 to your project.

### Koin support
1. Build `:initialization-koin`.
2. Add `.aar` created in step 1 to your project.
3. Add `com.gft.initialization.di.initializationModule` to the Koin graph.

## Usage

1. Define any number of initializers by implementing the `Initializer` interface.
   ```kotlin
   class SomeInitializer : Initializer {
       override suspend fun initialize() {
           // do some stuff
       }
   }
   ```
   > ⚠ `initialize` method will run on the current thread. Use `withContext(dispatcher)` to perform long running operations or IO operations.

2. Define the initializers graph.
   ```kotlin
      listOf(
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
   ```
   Note a few things:
   - Initializers will run in the same order as defined in the list.
   - If you want to run a few initializers in parallel use `initializeInParallel`
    
