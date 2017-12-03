package com.github.takahirom.plugin;

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.BaseVariant
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

class DebugAlterPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.afterEvaluate {
            applyAfterEvaluate(project)
        }
    }

    private fun applyAfterEvaluate(project: Project) {
        val logger = project.logger
        val extension = project.extensions.findByName("android")
        val android = extension as? AppExtension ?: run {
            logger.error("DebugAlter plugin should use with android plugin")
            return
        }
        // Look for debug build variant
        android.applicationVariants.all { variant ->
            configVariant(variant, project, logger, android)
        }
    }

    private fun configVariant(variant: BaseVariant, project: Project, logger: Logger, android: AppExtension) {
        val variantName = variant.name[0].toUpperCase() + variant.name.substring(1, variant.name.length)
        val isTest = project.gradle
                .startParameter
                .taskNames
                .find { it.contains("UnitTest") }
                .orEmpty()
                .isNotEmpty()

        if (!variant.buildType.isDebuggable || isTest) {
            logger.info("Skipping AspectJ weaving non-debuggable or test build variant '${variantName}'.")
            return
        }

//        project.dependencies.add("debugImplementation","com.github.takahirom.debug.alter:library:0.1")
//        project.dependencies.add("releaseImplementation","com.github.takahirom.debug.alter:library:0.1")

        createLogDirectoryIfNeeded(project)

        val javaCompile: JavaCompile = variant.javaCompile

        val kotlinTaskName = "compile" + variantName + "Kotlin"
        val kotlinCompileTask: KotlinCompile? = project.tasks.findByName(kotlinTaskName) as? KotlinCompile
        val allClassPath = if (kotlinCompileTask == null) {
            logger.error("Could not get kotlin task (${kotlinTaskName}) in aspect.gradle")
            project.files(javaCompile.destinationDir,
                    javaCompile.classpath).asPath
        } else {
            project.files(kotlinCompileTask.destinationDir, javaCompile.destinationDir,
                    javaCompile.classpath).asPath
        }


        javaCompile.doLast {
            logger.lifecycle("Java file weaving by AspectJ")

            val start = System.currentTimeMillis()
            val javaArgs = arrayOf<String>("-showWeaveInfo",
                    "-1.8",
                    "-inpath", javaCompile.destinationDir.absolutePath,
                    // It should contains AspectJ path
                    "-aspectpath", allClassPath,
                    "-d", javaCompile.destinationDir.absolutePath,
                    "-classpath", allClassPath,
                    "-log", "log/weave.log",
                    "-bootclasspath", android.bootClasspath.joinToString("/"))
            weave(project, javaArgs)

            val now = System.currentTimeMillis()

            logger.info("Java file weaving took ${now - start} ms")
        }

        if (kotlinCompileTask != null) {
            val kotlinClassDir = kotlinCompileTask.destinationDir
            val kotlinClassDirPath = kotlinClassDir.absolutePath

            kotlinCompileTask.doLast {
                logger.lifecycle("Kotlin file weaving by AspectJ")


                val start = System.currentTimeMillis()
                // https://www.eclipse.org/aspectj/doc/next/devguide/ajc-ref.html
                val kotlinArgs = arrayOf<String>("-showWeaveInfo",
                        "-1.8",
                        "-inpath", kotlinClassDirPath,
                        "-aspectpath", allClassPath,
                        "-d", kotlinClassDir.toString(),
                        "-classpath", allClassPath,
                        "-log", "log/weave.log",
                        "-bootclasspath", android.bootClasspath.joinToString("/"))
                weave(project, kotlinArgs)
                val now = System.currentTimeMillis()

                logger.info("Kotlin file weaving took ${now - start} ms")
            }

        }
    }

    private fun createLogDirectoryIfNeeded(project: Project) {
        val logDir = project.rootProject.file("log")
        if (!logDir.exists()) {
            logDir.mkdir()
        }
    }

    fun weave(project: Project, args: Array<String>) {
        val handler = MessageHandler(true)
        project.logger.info("weave args: " + Arrays.toString(args))
        Main().run(args, handler)

        handler.getMessages(null, true).forEach { message ->
            when (message.kind) {
                IMessage.ABORT -> project.logger.error(message.message, message.thrown)
                IMessage.ERROR -> project.logger.error(message.message, message.thrown)
                IMessage.FAIL -> project.logger.error(message.message, message.thrown)
                IMessage.WARNING -> project.logger.warn(message.message, message.thrown)
                IMessage.INFO -> project.logger.info(message.message, message.thrown)
                IMessage.DEBUG -> project.logger.debug(message.message, message.thrown)
                else -> {
                }
            }

        }
    }
}
