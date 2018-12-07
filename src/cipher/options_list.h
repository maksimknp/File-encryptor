// clang-format off
//         variable    type      default  option name    arg name     description
OPTION_DEF(encrypt,     bool,     false,   "--encrypt",   nullptr,     "Encrypting files")
OPTION_DEF(decrypt,     bool,     false,   "--decrypt",   nullptr,     "Decrypting files")
OPTION_DEF(file,        char *,   nullptr, "--file",      "file",      "File name")
OPTION_DEF(keyPath,    char *,   nullptr, "--key-path",  "keyPath",   "path to Key for encryption")
// clang-format on
