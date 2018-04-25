package com.haretskiy.pavel.magiccamera

const val BUNDLE_KEY_SIGN = "sign_in_bundle"
const val SIGN_IN_FLAG = "flag_sign_in"
const val SIGN_UP_FLAG = "flag_sign_up"

const val SIGN_IN = "SIGN IN"
const val SIGN_UP = "SIGN UP"

const val PASSWORDS_DO_NOT_MATCH = "Passwords doesn't match"
const val FIELDS_ARE_EMPTY = "Password and email fields mustn't be empty"
const val EMPTY_STRING = ""
const val KEY_BUNDLE_TOKEN = "token_user_firebase"

const val BUNDLE_KEY_IS_LOG_IN = "is_user_log_in"

//Camera2 constants
const val CODE_REQUEST_CAMERA_PERMISSION = 1
const val PIC_FILE_NAME = "photo.jpg"

const val FRAGMENT_DIALOG_COMP = "Compl_dialog"
const val FRAGMENT_DIALOG_SETTINGS = "settings_dialog"
const val FRAGMENT_DIALOG_ERROR = "error_dialog"

const val TAG = "Camera2BasicFragment"

//camera state: Showing camera preview.
const val STATE_PREVIEW = 0
//Camera state: Waiting for the focus to be locked.
const val STATE_WAITING_LOCK = 1
//Camera state: Waiting for the exposure to be precapture state.
const val STATE_WAITING_PRECAPTURE = 2
//Camera state: Waiting for the exposure state to be something other than precapture.
const val STATE_WAITING_NON_PRECAPTURE = 3
//Camera state: Picture was taken.
const val STATE_PICTURE_TAKEN = 4
//Max preview width that is guaranteed by Camera2 API
const val MAX_PREVIEW_WIDTH = 1920
//Max preview height that is guaranteed by Camera2 API
const val MAX_PREVIEW_HEIGHT = 1080

