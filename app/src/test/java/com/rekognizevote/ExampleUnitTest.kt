package com.rekognizevote

import org.junit.Test
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    
    @Test
    fun constants_areValid() {
        assertEquals("rekognize_vote_prefs", com.rekognizevote.core.Constants.PREFS_NAME)
        assertEquals(90f, com.rekognizevote.core.Constants.FACE_SIMILARITY_THRESHOLD)
        assertEquals("active", com.rekognizevote.core.Constants.POLL_STATUS_ACTIVE)
    }
}