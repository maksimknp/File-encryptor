#ifndef BLOW_FISH

#define BLOW_FISH

#include <cstdint>

class BlowFish final {

public:    

    uint64_t Decrypt(uint64_t cipher) const;
   
    uint64_t Encrypt(uint64_t plain) const;
   
    BlowFish(char* K)
    {
        InitializeSubKeys(K);
    }
    BlowFish(const BlowFish& that) = delete;
    BlowFish(BlowFish&& that) = delete;

private:

    inline uint32_t F(uint32_t xL) const
    {
        const uint64_t twoPow32 = 0x100000000ull;
        uint32_t part1 = xL >> 24,
                 part2 = (xL >> 16) & 0xfful,
                 part3 = (xL >> 8) & 0xfful,
                 part4 = xL & 0xfful;
    
        return ((((static_cast<uint64_t>(S[0][part1]) + 
                   static_cast<uint64_t>(S[1][part2])) % 
                   twoPow32) ^ static_cast<uint64_t>(S[2][part3])) +
                   static_cast<uint64_t>(S[3][part3])) % twoPow32;
    }

    void InitializeSubKeys(char* K);

private:
    const uint8_t KeySizeInWords = 8;
    //subkeys
    uint32_t P[18];
    uint32_t S[4][256];
};

#endif
