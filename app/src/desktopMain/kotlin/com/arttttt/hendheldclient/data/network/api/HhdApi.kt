package com.arttttt.hendheldclient.data.network.api

import com.arttttt.hendheldclient.data.network.model.settings.HhdSettingsApiResponse2
import com.arttttt.hendheldclient.data.network.model.state.HhdStateApiResponse
import com.arttttt.hendheldclient.data.network.model.state.HhdStateRequestBody
import com.arttttt.hendheldclient.domain.entity.HhdAuthToken
import com.arttttt.hendheldclient.domain.entity.HhdPort
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HhdApi(
    private val token: HhdAuthToken,
    private val port: HhdPort,
) {

    /**
     * todo: make address configurable
     */
    private val baseUrl = "http://127.0.0.1/api/v1/"

    private val client = HttpClient(CIO) {
        install(Logging) {
            this.level = LogLevel.ALL

            logger = object : Logger {
                override fun log(message: String) {
                    println(message)
                }
            }
        }
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(token.token, "")
                }
            }
        }

        defaultRequest {
            url(baseUrl)

            port = this@HhdApi.port.port
        }
    }

    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    suspend fun getSettings(): HhdSettingsApiResponse2 {
        return json.decodeFromString<HhdSettingsApiResponse2>(
            """{"controllers": {"legion_go": {"type": "container", "title": "Legion Controllers", "hint": "Allows for configuring the Legion controllers using the built in firmware commands and enabling emulation modes for various controller types.", "tags": ["lgc"], "default": null, "children": {"xinput": {"type": "mode", "title": "Emulation Mode (X-Input)", "hint": "Emulate different controller types when the Legion Controllers are in X-Input mode.", "tags": ["lgc_xinput"], "default": "dualsense", "modes": {"uinput": {"type": "container", "title": "Xbox", "hint": "Creates a virtual `Handheld Daemon Controller` that can be used normally in apps. Back buttons are supported but steam will not detect them. If Gyroscope or Accelerometer are enabled, a Motion device will be created as well (experimental; works in Dolphin).", "tags": ["lgc_emulation_uinput", "uinput"], "default": null, "children": {}}, "dualsense": {"type": "container", "title": "Dualsense", "hint": "Emulates the Dualsense Sony controller from the Playstation 5. Since this controller does not have paddles, the paddles are mapped to left and right touchpad clicks.", "tags": ["lgc_emulation_dualsense", "dualsense"], "default": null, "children": {"led_support": {"type": "bool", "title": "LED Support", "hint": "Passes through the LEDs to the controller, which allows games to control them.", "tags": [], "default": true}, "paddles_to_clicks": {"type": "bool", "title": "Paddles to Clicks", "hint": "Maps the paddles of the device to left and right touchpad clicks making them usable in Steam. If more than 2 paddles (e.g., Legion Go) uses the top ones. If extra buttons (e.g., Ayaneo, GPD), uses them instead.", "tags": [], "default": true}, "sync_gyro": {"type": "bool", "title": "Gyro to Mouse Fix", "hint": "In the latest steam update, `Gyro to Mouse [BETA]` misbehaves if a report is sent without a new imu timestamp. This option makes it so reports are sent only when there is a new gyro timestamp, effectively limiting the responsiveness of the controller to that of the IMU (e.g., for Legion Go 100hz instead of 400hz). If the IMU is not working, this setting will break the controller.", "tags": [], "default": false}, "bluetooth_mode": {"type": "bool", "title": "Bluetooth Mode", "hint": "Emulates the controller in bluetooth mode instead of USB mode. This is the default as it causes less issues with how apps interact with the controller. However, using USB mode can improve LED support (?) in some games. Test and report back!", "tags": ["advanced"], "default": true}}}, "dualsense_edge": {"type": "container", "title": "Dualsense Edge", "hint": "Emulates the expensive Dualsense Sony controller which enables paddle support. The edge controller is a bit obscure, so some games might not support it correctly.", "tags": ["lgc_emulation_dualsense_edge", "dualsense_edge"], "default": null, "children": {"led_support": {"type": "bool", "title": "LED Support", "hint": "Passes through the LEDs to the controller, which allows games to control them.", "tags": [], "default": true}, "sync_gyro": {"type": "bool", "title": "Gyro to Mouse Fix", "hint": "In the latest steam update, `Gyro to Mouse [BETA]` misbehaves if a report is sent without a new imu timestamp. This option makes it so reports are sent only when there is a new gyro timestamp, effectively limiting the responsiveness of the controller to that of the IMU (e.g., for Legion Go 100hz instead of 400hz). If the IMU is not working, this setting will break the controller.", "tags": [], "default": false}, "bluetooth_mode": {"type": "bool", "title": "Bluetooth Mode", "hint": "Emulates the controller in bluetooth mode instead of USB mode. This is the default as it causes less issues with how apps interact with the controller. However, using USB mode can improve LED support (?) in some games. Test and report back!", "tags": ["advanced"], "default": true}}}}}, "gyro": {"type": "bool", "title": "Gyroscope", "hint": "Enables gyroscope support (.3% background CPU use)", "tags": [], "default": true}, "accel": {"type": "bool", "title": "Accelerometer", "hint": "Enables accelerometer support (CURRENTLY BROKEN; interferes with gyro;  if someone asks it will be fixed).", "tags": ["advanced", "expert"], "default": false}, "gyro_fix": {"type": "discrete", "title": "Gyro Hz", "hint": "Adds polling to the legion go gyroscope, to fix the low polling rate (required for gyroscope support). Set to 0 to disable. Due to hardware limitations, there is a marginal difference above 100hz.", "tags": [], "default": 100, "options": [0, 40, 60, 75, 100, 125, 200, 300]}, "gyro_scaling": {"type": "int", "title": "Gyro Scale", "hint": "Applies a scaling factor to the legion go gyroscope (since it is misconfigured by the driver). Try different values to see what works best. Low values cause a deadzone and high values will clip when moving the  Go abruptly.", "tags": [], "default": 22, "min": 15, "max": 40, "step": null, "unit": null, "smin": null, "smax": null}, "swap_legion": {"type": "multiple", "title": "Swap Legion with Start/Select", "hint": "Swaps the legion buttons with start select.", "tags": [], "default": "disabled", "options": {"disabled": "Disabled", "l_is_start": "Left is Start", "l_is_select": "Left is Select"}}, "nintendo_mode": {"type": "bool", "title": "Nintendo Mode (A-B Swap)", "hint": "Swaps A with B and X with Y.", "tags": [], "default": false}, "m2_to_mute": {"type": "bool", "title": "M2 As Mute", "hint": "Maps the M2 to the mute button on Dualsense and the HAPPY_TRIGGER_20 on Xbox.", "tags": [], "default": false}, "select_reboots": {"type": "bool", "title": "Hold Select to Reboot", "hint": "", "tags": [], "default": true}, "share_to_qam": {"type": "bool", "title": "Legion R to QAM", "hint": "", "tags": [], "default": true}, "touchpad": {"type": "mode", "title": "Touchpad Emulation", "hint": "Use an emulated touchpad. Part of the controller if it is supported (e.g., Dualsense) or a virtual one if not.", "tags": ["touchpad"], "default": "emulation", "modes": {"disabled": {"type": "container", "title": "Disabled", "hint": "Does not modify the touchpad. Short + holding presses will not work within gamescope.", "tags": [], "default": null, "children": {}}, "emulation": {"type": "container", "title": "Virtual", "hint": "Adds an emulated touchpad. This touchpad is meant to be for desktop use and has left, right click support by default, within gamescope and outside of it, regardless of the \"Tap to Click\" setting.", "tags": [], "default": null, "children": {"short": {"type": "multiple", "title": "Short Action", "hint": "Maps short touches (less than 0.2s) to a virtual touchpad button.", "tags": ["advanced"], "default": "left_click", "options": {"disabled": "Disabled", "left_click": "Left Click", "right_click": "Right Click"}}, "hold": {"type": "multiple", "title": "Hold Action", "hint": "Maps long touches (more than 2s) to a virtual touchpad button.", "tags": ["advanced"], "default": "right_click", "options": {"disabled": "Disabled", "left_click": "Left Click", "right_click": "Right Click"}}}}, "controller": {"type": "container", "title": "Controller", "hint": "Uses the touchpad of the emulated controller (if it exists). Otherwise, the touchpad remains unmapped (will still show up in the system). Meant to be used as steam input, so short press is unassigned by default and long press simulates trackpad click.", "tags": [], "default": null, "children": {"correction": {"type": "multiple", "title": "Correction Type", "hint": "The legion touchpad is square, whereas the DS5 one is rectangular. Therefore, it needs to be corrected. \"Contain\" maintain the whole DS5 touchpad and part of the Legion one is unused. \"Crop\" uses the full legion touchpad, and limits the area of the DS5. \"Stretch\" uses both fully (distorted). \"Crop End\" enables use in steam input as the right touchpad.", "tags": [], "default": "crop_end", "options": {"stretch": "Stretch", "crop_center": "Crop Center", "crop_start": "Crop Start", "crop_end": "Crop End", "contain_start": "Contain Start", "contain_end": "Contain End", "contain_center": "Contain Center"}}, "short": {"type": "multiple", "title": "Short Action", "hint": "Maps short touches (less than 0.2s) to a touchpad action. Dualsense uses a physical press for left and a double tap for right click (support for double tap varies; enable \"Tap to Click\" in your desktop's touchpad settings).", "tags": ["advanced"], "default": "disabled", "options": {"disabled": "Disabled", "left_click": "Left Click", "right_click": "Right Click"}}, "hold": {"type": "multiple", "title": "Hold Action", "hint": "Maps long touches (more than 2s) to a touchpad action. Dualsense uses a physical press for left and a double tap for right click (support for double tap varies; enable \"Tap to Click\" in your desktop's touchpad settings).", "tags": [], "default": "left_click", "options": {"disabled": "Disabled", "left_click": "Left Click", "right_click": "Right Click"}}}}}}, "shortcuts": {"type": "bool", "title": "Enable Shortcuts Controller", "hint": "When in other modes (dinput, dual dinput, and fps), enable a shortcuts  controller to restore Guide, QAM, and shortcut functionality.", "tags": [], "default": true}, "debug": {"type": "bool", "title": "Debug", "hint": "Output controller events to the console (high CPU use) and raises exceptions (HHD will crash on errors).", "tags": ["advanced", "expert"], "default": false}}}}, "hhd": {"settings": {"type": "container", "title": "About", "hint": "", "tags": [], "default": null, "children": {"powerbuttond": {"type": "bool", "title": "Steam Powerbutton Handler", "hint": "Enables the Steam Powerbutton handler (responsible for the wink and powerbutton menu).", "tags": [], "default": true}, "decky_deprecation": {"type": "display", "title": "It is no longer possible to update Decky from here. If you see this, update the Decky plugin manually.", "hint": "", "tags": ["hhd-version-display-decky", "text"], "default": " "}, "version": {"type": "display", "title": "Handheld Daemon Version", "hint": "Displays the Handheld Daemon version.", "tags": ["hhd-version-display", "text"], "default": null}, "update_stable": {"type": "action", "title": "Update (Stable)", "hint": "Updates to the latest version from PyPi (local install only).", "tags": ["hhd-update-stable"], "default": null}, "update_beta": {"type": "action", "title": "Update (Unstable)", "hint": "Updates to the master branch from git (local install only).", "tags": ["hhd-update-beta"], "default": null}}}, "http": {"type": "container", "title": "API Configuration", "hint": "Settings for configuring the http endpoint of HHD.", "tags": ["hhd-http", "advanced", "expert"], "default": null, "children": {"enable": {"type": "bool", "title": "Enable the API", "hint": "Enables the API of Handheld Daemon (required for decky and ui).", "tags": ["advanced"], "default": true}, "port": {"type": "int", "title": "API Port", "hint": "Which port should the API be on?", "tags": ["advanced", "hhd-port", "port", "dropdown"], "default": 5335, "min": 1024, "max": 49151, "step": null, "unit": null, "smin": null, "smax": null}, "localhost": {"type": "bool", "title": "Limit Access to localhost", "hint": "Sets the API target to '127.0.0.1' instead '0.0.0.0'.", "tags": ["advanced"], "default": true}, "token": {"type": "bool", "title": "Use Security token", "hint": "Generates a security token in `~/.config/hhd/token` that is required for authentication.", "tags": ["advanced"], "default": true}}}, "version": {"type": "version", "value": "24f8cefa"}}}"""
        )

        //return client.get("settings").body()
    }

    suspend fun getState(): HhdStateApiResponse {
        return json.decodeFromString(
            """{"controllers": {"legion_go": {"xinput": {"mode": "dualsense_edge", "dualsense": {"led_support": true, "paddles_to_clicks": true, "sync_gyro": false, "bluetooth_mode": true}, "dualsense_edge": {"led_support": true, "sync_gyro": false, "bluetooth_mode": true}}, "gyro": true, "accel": false, "gyro_fix": 100, "gyro_scaling": 22, "swap_legion": "disabled", "nintendo_mode": false, "m2_to_mute": false, "select_reboots": false, "share_to_qam": true, "touchpad": {"mode": "controller", "emulation": {"short": "left_click", "hold": "right_click"}, "controller": {"correction": "crop_end", "short": "left_click", "hold": "right_click"}}, "shortcuts": true, "debug": false}}, "hhd": {"settings": {"powerbuttond": true, "decky_deprecation": " ", "version": "1.3.10", "update_stable": false, "update_beta": false}, "http": {"enable": true, "port": 5335, "localhost": true, "token": true}}, "version": "24f8cefa"}"""
        )

        //return client.get("state").body()
    }

    suspend fun setState(body: HhdStateRequestBody) {
        client.post("state") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }
}