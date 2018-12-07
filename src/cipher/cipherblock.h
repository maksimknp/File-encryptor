#ifndef CIPHER_BLOCK

#define CIPHER_BLOCK
#include "blowfish.h"
#include <fstream>

class CipherBlockEncryptor final {

public:
    CipherBlockEncryptor(const char* keyPath): K(GetKey(keyPath)), encryptor(K)
    {}
    ~CipherBlockEncryptor()
    {
        delete[] K;
    }
    CipherBlockEncryptor(const CipherBlockEncryptor& that) = delete;
    CipherBlockEncryptor(CipherBlockEncryptor&& that) = delete;

    void EncryptFile(const char* fileName) const;

    void DecryptFile(const char* fileName) const;

private:
    void GenerateVector(uint64_t count) const;
    char* GetKey(const char* keyPath);
private:
    char* K;
    BlowFish encryptor;
    mutable uint64_t xorVector[2];
};
#endif
