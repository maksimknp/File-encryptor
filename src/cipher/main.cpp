#include "cipherblock.h"
#include "options.h"

int main(int argc, char* argv[])
{
    options::ParseOptions(argc, argv);
    
    std::fstream file;
    file.open(options::file, std::fstream::binary | std::fstream::in | std::fstream::out);
    
    CipherBlockEncryptor encryptor(options::key);

    if (options::encrypt)
    {
        encryptor.EncryptFile(file);
    }
    if (options::decrypt)
    {
        encryptor.DecryptFile(file);
    }

    return 0;
}
