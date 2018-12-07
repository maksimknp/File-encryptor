#include "cipherblock.h"
#include <cassert>

void CipherBlockEncryptor::EncryptFile(const char* fileName) const
{
    std::fstream file;
    file.open(fileName, std::fstream::binary | std::fstream::in | std::fstream::out);
    assert(file.is_open());
    
    file.seekg(0, file.end);
    uint64_t fileSize = file.tellg();
    file.seekg(0, file.beg);

    int8_t lastBlockSize = (fileSize + 1) % 16ul;
    uint8_t numAppendBytes = (lastBlockSize == 0 ? 0 : 16 - lastBlockSize);
    uint64_t newSize = fileSize + numAppendBytes + 1;
    uint64_t numBlocks = (newSize) / 16ul;
    char* buffer = new char[newSize];

    file.read(buffer + 1, fileSize);
    file.seekg(0, file.beg);

    buffer[0] = numAppendBytes;
    for (int i = 0; i < numAppendBytes; ++i)
    {
        buffer[fileSize + 1 + i] = 0;
    }
    
    GenerateVector(newSize);
    uint64_t* workBuf = reinterpret_cast<uint64_t*>(buffer);
    for (uint64_t i = 0; i < numBlocks; ++i)
    {
        workBuf[2 * i] = encryptor.Encrypt(workBuf[2 * i] ^ xorVector[0]);
        xorVector[0] = workBuf[2 * i];
        workBuf[2 * i + 1] = encryptor.Encrypt(workBuf[2 * i + 1] ^ xorVector[1]);
        xorVector[1] = workBuf[2 * i + 1];
    }
    file.write(buffer, newSize);
    file.close();

    delete[] buffer;
}

void CipherBlockEncryptor::DecryptFile(const char* fileName) const
{
    std::fstream file;
    file.open(fileName, std::fstream::binary | std::fstream::in | std::fstream::out);
    assert(file.is_open());

    file.seekg(0, file.end);
    uint64_t fileSize = file.tellg();
    file.seekg(0, file.beg);

    char* buffer = new char[fileSize];

    file.read(buffer, fileSize);
    file.close();

    file.open(fileName, std::fstream::binary | 
                        std::fstream::in | 
                        std::fstream::out | 
                        std::fstream::trunc);
    assert(file.is_open());
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
    GenerateVector(fileSize);
    workBuf[0] = encryptor.Decrypt(workBuf[0]) ^ xorVector[0];
    workBuf[1] = encryptor.Decrypt(workBuf[1]) ^ xorVector[1];
    
    file.write(buffer + 1, fileSize - buffer[0] - 2);
    file.close();

    delete[] buffer;
}

char* CipherBlockEncryptor::GetKey(const char* keyPath)
{
    std::fstream file;
    file.open(keyPath, std::fstream::in | std::fstream::out | std::fstream::binary);
    assert(file.is_open());

    char* key = new char[32];
    file.read(key, 32);

    file.close();
    return key;
}

void CipherBlockEncryptor::GenerateVector(uint64_t count) const
{
    xorVector[0] = xorVector[1] = 0;
    uint8_t idx = count % 32;
    for (int8_t i = 0; i < 2; ++i)
    {
        for (int8_t j = 0; j < 8; ++j)
        {
            xorVector[i] |= static_cast<uint64_t>(K[idx]) << (j * 8);
            idx = (idx + 1) % 32;
        }
    }
}
