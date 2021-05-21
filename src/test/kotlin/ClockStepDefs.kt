import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.lessThan
import io.cucumber.java.de.Dann
import io.cucumber.java.de.Wenn
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import java.time.LocalDateTime

class ClockStepDefs {
    private lateinit var time: LocalDateTime

    @When("we look at the clock")
    @Wenn("wir die Uhr ablesen")
    fun lookAtClock() {
        time = LocalDateTime.now()
    }

    @Then("it should be before {int}h")
    @Dann("sollte es vor {int} Uhr sein")
    fun hourShouldBeBefore(hour: Int) {
        assertThat(time.hour, lessThan(hour))
    }
}
