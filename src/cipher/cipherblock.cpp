#include "cipherblock.h"
#include <cassert>

void CipherBlockEncryptor::EncryptFile(std::fstream& file) const
{
    assert(file.is_open());
    file.seekg(0, file.end);
    uint64_t fileSize = file.tellg();
    file.seekg(0, file.beg);

    char* buffer = new char[fileSize];

    file.read(buffer, fileSize);
    file.seekg(0, file.beg);

    uint64_t numBlocks = fileSize / 16ul;
    GenerateVector(/*TODO ARGS*/);
    uint64_t* workBuf = reinterpret_cast<uint64_t*>(buffer);
    for (uint64_t i = 0; i < numBlocks; ++i)
    {
        workBuf[2 * i] = encryptor.Encrypt(workBuf[2 * i] ^ xorVector[0]);
        xorVector[0] = workBuf[2 * i];
        workBuf[2 * i + 1] = encryptor.Encrypt(workBuf[2 * i + 1] ^ xorVector[1]);
        xorVector[1] = workBuf[2 * i + 1];
    }
    //TODO
    //encrypt tail
    file.write(buffer, fileSize);
    file.close();

    delete[] buffer;
}

void CipherBlockEncryptor::DecryptFile(std::fstream& file) const
{
    assert(file.is_open());
    file.seekg(0, file.end);
    uint64_t fileSize = file.tellg();
    file.seekg(0, file.beg);

    char* buffer = new char[fileSize];

    file.read(buffer, fileSize);
    file.seekg(0, file.beg);

    uint64_t numBlocks = fileSize / 16ul;
    uint64_t* workBuf = reinterpret_cast<uint64_t*>(buffer);
    for (uint64_t i = numBlocks - 1; i != 0; --i)
    {
        xorVector[0] = workBuf[2 * (i - 1)];
        xorVector[1] = workBuf[2 * (i - 1) + 1];
        workBuf[2 * i] = encryptor.Decrypt(workBuf[2 * i]) ^ xorVector[0];
        workBuf[2 * i + 1] = encryptor.Decrypt(workBuf[2 * i + 1]) ^ xorVector[1];
    }
    GenerateVector(/*TODO ARGS*/);
    workBuf[0] = encryptor.Decrypt(workBuf[0]) ^ xorVector[0];
    workBuf[1] = encryptor.Decrypt(workBuf[1]) ^ xorVector[1];
    //TODO
    //decrypt tail
    file.write(buffer, fileSize);
    file.close();

    delete[] buffer;
}

void CipherBlockEncryptor::GenerateVector() const
{
    xorVector[0] = 0x0ull;
    xorVector[1] = 0xffffffffull;
}
