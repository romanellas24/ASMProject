include "math.iol"



define generateRandomSeed {
    random@Math(void)(seed);
    roundRequest = seed * 25000;
    roundRequest.decimals = 10;
    round@Math(roundRequest)(seed)
}