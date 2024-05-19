import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.onTimeout
import kotlinx.coroutines.selects.select
import org.bakalover.iot.message.Request
import org.bakalover.iot.pipeline.ISwitch
import org.bakalover.iot.pipeline.Switch
import org.bakalover.iot.process.Consumer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class ConsumerTests {

    private lateinit var c: Consumer
    private lateinit var from: ISwitch<Request>
    private lateinit var to: ISwitch<Request>

    @BeforeEach
    fun prepare() {
        from = Switch(7)
        to = Switch(7)
        c = Consumer()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun houseRouting() {
        val c = Consumer()
        val tasks = 100000

        runBlocking {
            launch {
                (0..<tasks).forEach {
                    from.getChannel(it % 7).send(Request(it % 7, "test", "test"))
                }
            }

            launch {
                c.doProcess(from, to, this)
            }

            val check = launch {
                var jobCount = 0
                while (true) {
                    select {
                        to.getAll().forEachIndexed { id, channel ->
                            channel.onReceive { task ->
                                assertTrue { task.houseId == id }
                                ++jobCount
                            }
                        }
                        onTimeout(3000) {
                            return@onTimeout
                        }
                    }

                    if (jobCount == tasks) {
                        (0..<7).forEach {
                            assertTrue { to.getChannel(it).isEmpty }
                        }
                        c.abort()
                        break
                    }
                }
            }
            check.join()
        }
    }
}