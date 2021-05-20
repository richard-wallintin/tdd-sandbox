import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.lessThan
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import java.time.LocalDateTime

class BasicStepDefs {
    private lateinit var time: LocalDateTime

    @When("we look at the clock")
    fun `we look at the clock`() {
        time = LocalDateTime.now()
    }

    @Then("it should be before {int}h")
    fun `it should be before midnight`(hour: Int) {
        assertThat(time.hour, lessThan(hour))
    }
}
