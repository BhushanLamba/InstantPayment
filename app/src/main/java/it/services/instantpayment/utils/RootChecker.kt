package it.services.instantpayment.utils

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object RootChecker{


    // Check for Superuser APK
    private fun checkForSuperuserApk(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/system/app/SuperSU.apk",
            "/system/app/MagiskManager.apk",
            "/system/app/Magisk.apk"
        )
        for (path in paths) {
            if (File(path).exists()) {
                return true
            }
        }
        return false
    }

    // Check for SU binary
    private fun checkForSuBinary(): Boolean {
        val paths = arrayOf(
            "/system/bin/",
            "/system/xbin/",
            "/sbin/",
            "/system/sd/xbin/",
            "/system/bin/failsafe/",
            "/data/local/xbin/",
            "/data/local/bin/",
            "/data/local/"
        )
        for (path in paths) {
            if (File(path + "su").exists()) {
                return true
            }
        }
        return false
    }

    // Execute SU command
    private fun checkExecuteSuCommand(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val input = BufferedReader(InputStreamReader(process.inputStream))
            input.readLine() != null
        } catch (e: Exception) {
            false
        }
    }

    // Check build tags
    private fun checkBuildTags(): Boolean {
        val buildTags = android.os.Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    fun isDeviceRooted(): Boolean {
        return checkForSuperuserApk() || checkForSuBinary() || checkExecuteSuCommand() || checkBuildTags()
    }
}