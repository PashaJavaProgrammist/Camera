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
const val BUNDLE_KEY_URI_TO_ACTIVITY_DETAIL = "BUNDLE_KEY_URI_TO_ACTIVITY_DETAIL"
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
const val BUNDLE_KEY_URI_TO_FRAGMENT_DETAIL = "BUNDLE_KEY_URI_TO_FRAGMENT_DETAIL"
const val BUNDLE_KEY_DATE_TO_FRAGMENT_DETAIL = "BUNDLE_KEY_DATE_TO_FRAGMENT_DETAIL"
const val BUNDLE_KEY_BARCODE_RESULT = "BUNDLE_KEY_BARCODE_RESULT"
const val BUNDLE_DIALOG_DELETE_QR_CODE = "BUNDLE_DIALOG_DELETE_QR_CODE"
const val BUNDLE_DIALOG_DELETE_IS_QR_DETAIL = "BUNDLE_DIALOG_DELETE_IS_QR_DETAIL"
const val BUNDLE_KEY_QR_DETECTOR_STATE = "BUNDLE_KEY_QR_DETECTOR_STATE"
const val BUNDLE_KEY_TYPE_CLEAR_DB = "BUNDLE_KEY_TYPE_CLEAR_DB"
const val BUNDLE_KEY_URI_TO_ACTIVITY_SCAN = "BUNDLE_KEY_URI_TO_ACTIVITY_SCAN"

//Camera2 constants
const val CODE_REQUEST_CAMERA_PERMISSION = 1
const val PIC_FILE_NAME = "photo.jpg"

const val FRAGMENT_DIALOG_COMP = "Compl_dialog"
const val FRAGMENT_DIALOG_DELETE = "DELEte_dialog"
const val DELETE_QR_DIALOG = "DELETE_QR_DIALOG"
const val CLEAR_DB_DIALOG = "CLEAR_DB_DIALOG"
const val DELETE_ACCOUNT_DIALOG = "DELETE_ACCOUNT_DIALOG"

const val TYPE_QR = "TYPE_QR"
const val TYPE_PHOTO = "TYPE_PHOTO"

const val TAG = "Camera2BasicFragment"
const val PACKAGE_SETTINGS = "package:"
const val SUCCESSFUL_SAVING = "Photo saved:\n"
const val SUCCESSFUL_DELETING = "Photo is deleted"

const val SIZE_FILE = "\nSize: "
const val ERROR_SAVING = "Error of saving..\n"
const val CURRENT_USER = "Current user:"

const val KILOBYTES = " kilobytes"
const val PRINT = "Print photo"
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
const val SMILE_LEVEL_NONE = "none"
const val SMILE_LEVEL_LOW = "low"
const val SMILE_LEVEL_MIDDLE = "medium"
const val SMILE_LEVEL_HIGH = "high"
const val SMILE_LEVEL_VERY_HIGH = "very high"

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

const val BARCODE_SCAN_DELAY = 2000
const val SHARE_TYPE_TEXT = "text/plain"
const val SHARE_TYPE_IMAGE = "image/*"
const val FILE_PROVIDER_AUTHORITY = "com.haretskiy.pavel.magiccamera.providers.fileprovider"

const val URL_REGEX1 = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$"
const val URL_REGEX2 = "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)" +
        "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*" +
        "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)"
const val URL_DO_NOT_FOUND = "Url doesn't found"
const val OPEN_CHROME_TABS = "Can't open this in Chrome custom tabs. Press the link to try to open it in another browser"

const val PATTERN_EMAIL1 = "^[A-Za-z0-9][A-Za-z0-9.\\-_]*[A-Za-z0-9]*@([A-Za-z0-9]+([A-Za-z0-9-]*[A-Za-z0-9]+)*\\.)+[A-Za-z]*$"
const val PATTERN_EMAIL2 = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"