import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.bakalover.iot.pipeline.Switch
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class SwitchTests {

    @Test
    fun justSend() {
        val switch = Switch<Unit>(1)
        runBlocking {
            switch.getChannel(0).send(Unit)
            switch.getChannel(0).send(Unit)
            switch.getChannel(0).send(Unit)
        }
    }

    @Test
    fun panicOnChannel() {
        val switch = Switch<Unit>(1)
        assertThrows<IndexOutOfBoundsException> {
            runBlocking {
                switch.getChannel(1)
            }
        }
    }

    @Test
    fun sendReceive() {
        val switch = Switch<Unit>(1);
        runBlocking {
            switch.getChannel(0).send(Unit);
            switch.getChannel(0).receive()
        }
    }

    @Test
    fun concurrentRouting() {
        val switch = Switch<Unit>(2)
        val tasks = 1000000

        runBlocking {
            launch {
                (0..<tasks).forEach {
                    switch.getChannel(it % 2).send(Unit)
                }
                switch.getChannel(0).close()
                switch.getChannel(1).close()
            }

            val consumer1 = async {
                var localCount = 0
                for (i in switch.getChannel(0)) {
                    ++localCount
                }
                localCount
            }


            val consumer2 = async {
                var localCount = 0
                for (i in switch.getChannel(1)) {
                    ++localCount
                }
                localCount
            }
            assertEquals(consumer1.await() + consumer2.await(), tasks)
        }
    }
}