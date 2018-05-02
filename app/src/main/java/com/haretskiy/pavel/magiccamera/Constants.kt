package com.haretskiy.pavel.magiccamera

const val BUNDLE_KEY_SIGN = "sign_in_bundle"
const val SIGN_IN_FLAG = "flag_sign_in"
const val SIGN_UP_FLAG = "flag_sign_up"

const val SIGN_IN = "SIGN IN"
const val SIGN_UP = "SIGN UP"

const val SIGN_OUT_CODE = "sign_out"

const val PASSWORDS_DO_NOT_MATCH = "Passwords doesn't match"
const val FIELDS_ARE_EMPTY = "Password and email fields mustn't be empty"
const val EMPTY_STRING = ""

const val BUNDLE_KEY_CURRENT_CAMERA_ID = "current_camera_id"
const val BUNDLE_KEY_URI_TO_DETAIL = "BUNDLE_KEY_URI_TO_DETAIL"
const val BUNDLE_KEY_EMAIL = "BUNDLE_KEY_EMAIL"
const val BUNDLE_KEY_LAST_URI = "BUNDLE_KEY_LAST_URI"
const val BUNDLE_KEY_IS_LOG_IN = "is_user_log_in"
const val BUNDLE_KEY_PREFS_CAMERA_SIZE = "camera_size_prefs"
const val BUNDLE_KEY_CAMERA1_ID = "BUNDLE_KEY_CAMERA1_ID"
const val BUNDLE_KEY_DATA_TO_DETAIL = "BUNDLE_KEY_DATA_TO_DETAIL"
const val BUNDLE_KEY_FRAGMENT_ID = "BUNDLE_KEY_FRAGMENT_ID"
const val BUNDLE_KEY_CAMERA_TYPE_STATE = "BUNDLE_KEY_CAMERA_TYPE_STATE"
const val BUNDLE_DIALOG_DELETE_IS_PHOTO_DETAIL = "BUNDLE_DIALOG_DELETE_IS_PHOTO_DETAIL"
const val BUNDLE_DIALOG_DELETE_URI = "BUNDLE_DIALOG_DELETE_URI"

//Camera2 constants
const val CODE_REQUEST_CAMERA_PERMISSION = 1
const val PIC_FILE_NAME = "photo.jpg"

const val FRAGMENT_DIALOG_COMP = "Compl_dialog"
const val FRAGMENT_DIALOG_DELETE = "DELEte_dialog"

const val TAG = "Camera2BasicFragment"
const val PACKAGE_SETTINGS = "package:"
const val SUCCESSFUL_SAVING = "Photo saved:\n"
const val SUCCESSFUL_DELETING = "Photo deleted"
const val SIZE_FILE = "\nSize: "
const val ERROR_SAVING = "Error of saving..\n"

const val KILOBYTES = " kilobytes"

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

const val TWO_CAMERAS = 2
const val ONE_CAMERA = 1
const val NO_CAMERA = 0
const val CAMERA_TYPE_NOT_FOUND = -1

//Camera constants
const val FULL_SCREEN = true

//Google vision constants
const val RC_HANDLE_GMS = 9001
//const val FACE_POSITION_RADIUS = 10.0f
const val ID_TEXT_SIZE = 40.0f
const val ID_Y_OFFSET = 50.0f
const val ID_X_OFFSET = -50.0f
const val BOX_STROKE_WIDTH = 5.0f

const val CAMERA_FPS = 30.0f
const val MAX_PREVIEW_HEIGHT = 2560
const val MAX_PREVIEW_WIDTH = 1440

//db
const val DB_NAME = "camera_database"
//detail
const val AUTO_HIDE = true
const val AUTO_HIDE_DELAY_MILLIS = 3000
const val UI_ANIMATION_DELAY = 300
const val FORMAT_DATE = "dd.MM.yyyy HH:mm:ss"
//gallery
const val PAGE_SIZE = 15
const val PAGE_SIZE_HINT = 30
const val PREFETCH_DISTANCE = 6

