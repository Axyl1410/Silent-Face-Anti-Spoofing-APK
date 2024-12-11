package com.mv.engine

/**
 * Abstract class representing a component in the engine.
 * This class is responsible for loading the native library and provides abstract methods
 * for creating and destroying instances of the component.
 */
abstract class Component {

    init {
        try {
            // Load the native library named "engine"
            System.loadLibrary("engine")
            libraryFound = true
        } catch (e: UnsatisfiedLinkError) {
            // Print stack trace if the library is not found
            e.printStackTrace()
        }
    }

    /**
     * Abstract method to create an instance of the component.
     * @return A long value representing the instance.
     */
    abstract fun createInstance(): Long

    /**
     * Abstract method to destroy the component instance.
     */
    abstract fun destroy()

    companion object {
        /**
         * Boolean flag indicating whether the native library was successfully loaded.
         */
        var libraryFound: Boolean = false

        /**
         * Tag used for logging purposes.
         */
        const val tag = "Component"
    }
}