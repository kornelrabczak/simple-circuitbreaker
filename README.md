# simple-circuitbreaker
simple and thread-safe circuitbreaker implementation without HALF-OPEN state based on Hystrix implementation


Hystrix dependecy is used just for Sliding/Rolling Window implementation and maybe it's little overhead to use the whole library from Netflix but this is just example :) When I find some time I will probably make my own Sliding Window implementation to keep simplicity.  


# todo 

 * own implementation of sliding window algorithm
 * unit tests
 * extract interface for circuitbreaker and implement no op implementation
 * get rid of hardcoded configuration :(
