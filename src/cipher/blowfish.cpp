#include "blowfish.h"
#include <utility>


uint64_t BlowFish::Decrypt(uint64_t cipher) const
{
    uint32_t xL = cipher >> 32,
             xR = cipher & 0xffffffffull;

    xR ^= P[16];
    xL ^= P[17];
    xR ^= F(xL);
    xL ^= P[15];
    for (int i = 14; i >= 0; --i)
    {
        std::swap(xL, xR);
        xR ^= F(xL);
        xL ^= P[i];
    }
    return (static_cast<uint64_t>(xL) << 32) | 
                static_cast<uint64_t>(xR);
}

uint64_t BlowFish::Encrypt(uint64_t plain) const
{
    uint32_t xL = plain >> 32,
             xR = plain & 0xffffffffull;
    for (int i = 0; i < 15; ++i)
    {
        xL ^= P[i];
        xR ^= F(xL);
        std::swap(xL, xR);
    }
    xL ^= P[15];
    xR ^= F(xL);
    xR ^= P[16];
    xL ^= P[17];
    return (static_cast<uint64_t>(xL) << 32) | 
                static_cast<uint64_t>(xR);
}
 
void BlowFish::InitializeSubKeys(char* Key)
{
    uint32_t* K = reinterpret_cast<uint32_t*>(Key);

    uint8_t hexPi[] = { 0x3, 0x2, 0x4, 0x3, 0xf, 0x6, 0xa, 0x8, 0x8, 0x8, 0x5, 0xa,
                        0x3, 0x0, 0x8, 0xd, 0x3, 0x1, 0x3, 0x1, 0x9, 0x8, 0xa, 0x2,
                        0xe, 0x0, 0x3, 0x7, 0x0, 0x7, 0x3, 0x4, 0x4, 0xa, 0x4, 0x0,
                        0x9, 0x3, 0x8, 0x2, 0x2, 0x2, 0x9, 0x9, 0xf, 0x3, 0x1, 0xd,
                        0x0, 0x0, 0x8, 0x2, 0xe, 0xf, 0xa, 0x9, 0x8, 0xe, 0xc, 0x4,
                        0xe, 0x6, 0xc, 0x8, 0x9, 0x4, 0x5, 0x2, 0x8, 0x2, 0x1, 0xe,
                        0x3, 0x8, 0xd, 0x0, 0x1, 0x3, 0x7, 0x7, 0xb, 0xe, 0x5, 0x4,
                        0x6, 0x6, 0xc, 0xf, 0x3, 0x4, 0xe, 0x9, 0x0, 0xc, 0x6, 0xc,
                        0xc, 0x0, 0xa, 0xc};
    
    uint16_t hexPiSize = sizeof(hexPi);
    uint16_t piIdx = 0;
    uint8_t keyIdx = 0; 
    
    for (int i = 0; i < 18; ++i)
    {
        P[i] =  (static_cast<uint32_t>(hexPi[piIdx % hexPiSize]) << 28)       |
                (static_cast<uint32_t>(hexPi[(piIdx + 1) % hexPiSize]) << 24) |
                (static_cast<uint32_t>(hexPi[(piIdx + 2) % hexPiSize]) << 20) |
                (static_cast<uint32_t>(hexPi[(piIdx + 3) % hexPiSize]) << 16) |
                (static_cast<uint32_t>(hexPi[(piIdx + 4) % hexPiSize]) << 12) |
                (static_cast<uint32_t>(hexPi[(piIdx + 5) % hexPiSize]) << 8)  |
                (static_cast<uint32_t>(hexPi[(piIdx + 6) % hexPiSize]) << 4)  |
                (static_cast<uint32_t>(hexPi[(piIdx + 7) % hexPiSize]));
        P[i] ^= K[keyIdx];
        keyIdx = (keyIdx + 1) % KeySizeInWords;
        piIdx = (piIdx + 8) % hexPiSize;
    }
    for (int i = 0; i < 4; ++i)
    {
        for (int j = 0; j < 256; ++j)
        {
            S[i][j] =  (static_cast<uint32_t>(hexPi[piIdx % hexPiSize]) << 28)    |
                    (static_cast<uint32_t>(hexPi[(piIdx + 1) % hexPiSize]) << 24) |
                    (static_cast<uint32_t>(hexPi[(piIdx + 2) % hexPiSize]) << 20) |
                    (static_cast<uint32_t>(hexPi[(piIdx + 3) % hexPiSize]) << 16) |
                    (static_cast<uint32_t>(hexPi[(piIdx + 4) % hexPiSize]) << 12) |
                    (static_cast<uint32_t>(hexPi[(piIdx + 5) % hexPiSize]) << 8)  |
                    (static_cast<uint32_t>(hexPi[(piIdx + 6) % hexPiSize]) << 4)  |
                    (static_cast<uint32_t>(hexPi[(piIdx + 7) % hexPiSize]));
            piIdx = (piIdx + 8) % hexPiSize;
        }
    }

    uint64_t cipherWord = 0;
    for (int i = 0; i < 18; i += 2)
    {
        cipherWord = Encrypt(cipherWord);
        P[i] = cipherWord >> 32;
        P[i + 1] = cipherWord & 0xffffffffull;
    }
    for (int i = 0; i < 4; ++i)
    {
        for (int j = 0; j < 256; j += 2)
        {
            cipherWord = Encrypt(cipherWord);
            S[i][j] = cipherWord >> 32;
            S[i][j + 1] = cipherWord & 0xffffffffull;
        }
    }
}

