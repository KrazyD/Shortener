package shortener.service;

@FunctionalInterface
public interface TwoParamsFunction<A, B, C> {
    C apply(A a, B b);
}
