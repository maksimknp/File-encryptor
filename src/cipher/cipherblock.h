#ifndef CIPHER_BLOCK

#define CIPHER_BLOCK
#include "blowfish.h"
#include <fstream>

class CipherBlockEncryptor final {

public:
    CipherBlockEncryptor(char* Key): encryptor(Key)
    {}

    CipherBlockEncryptor(const CipherBlockEncryptor& that) = delete;
    CipherBlockEncryptor(CipherBlockEncryptor&& that) = delete;

    void EncryptFile(std::fstream& file) const;

    void DecryptFile(std::fstream& file) const;

private:
    void GenerateVector(/*TODO ARGS*/) const;

private:
    BlowFish encryptor;
    mutable uint64_t xorVector[2];
};
#endif
