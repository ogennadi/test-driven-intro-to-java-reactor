import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

class SourceTest {
    @Test
    void shouldEmit3ItemsAfterADelay() {
        StepVerifier.withVirtualTime(() -> new Source().emit())
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(10))
                .expectNextCount(3)
                .verifyComplete();
    }
}

class Source {
    public Flux<Integer> emit() {
        return Flux.just(1, 2, 3)
                .delaySequence(Duration.ofSeconds(10))
                .flatMap(x -> sideEffect().thenReturn(x));
    }

    private Mono<Void> sideEffect() {
        return Mono.delay(Duration.ofSeconds(0))
                .then(Mono.fromRunnable(() -> System.out.println(Thread.currentThread().getName())));
    }
}
