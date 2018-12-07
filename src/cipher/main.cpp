#include "cipherblock.h"
#include "options.h"

int main(int argc, char* argv[])
{
    options::ParseOptions(argc, argv);
    
    CipherBlockEncryptor encryptor(options::keyPath);

    if (options::encrypt)
    {
        encryptor.EncryptFile(options::file);
    }
    if (options::decrypt)
    {
        encryptor.DecryptFile(options::file);
    }

    return 0;
}
