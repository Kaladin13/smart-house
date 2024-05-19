import org.bakalover.iot.process.Backoff
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class BackoffTests {
    @Test
    fun backoffMonotonous() {
        val b = Backoff()
        for (i in 0..100) {
            assertTrue { b.nextDelay() < b.nextDelay() || b.nextDelay() > 2000 }
        }
    }

    @Test
    fun reset() {
        val b = Backoff()
        for (i in 0..100) {
            val first = b.nextDelay();
            b.reset()
            val second = b.nextDelay();
            assertTrue { second < first * 2 }
        }
    }
}