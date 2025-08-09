package com.rekognizevote.core

object Constants {
    const val PREFS_NAME = "rekognize_vote_prefs"
    const val ACCESS_TOKEN_KEY = "access_token"
    const val REFRESH_TOKEN_KEY = "refresh_token"
    const val USER_ID_KEY = "user_id"
    
    const val FACE_SIMILARITY_THRESHOLD = 90f
    const val MAX_IMAGE_SIZE_MB = 5
    const val CAMERA_CAPTURE_TIMEOUT = 30_000L
    
    const val POLL_STATUS_ACTIVE = "active"
    const val POLL_STATUS_UPCOMING = "upcoming"
    const val POLL_STATUS_CLOSED = "closed"
}