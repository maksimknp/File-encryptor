#include "options.h"
#include <cstdlib>
#include <stdexcept>
#include <string>

namespace options
{
#define OPTION_DEF(var, type, def, opt_name, arg_name, desc) \
    Option<type> var(def, opt_name, arg_name, desc);
#include "options_list.h"
#undef OPTION_DEF

void PrintHelp()
{
#define OPTION_DEF(var, type, def, opt_name, arg_name, desc) var.PrintHelp();
#include "options_list.h"
#undef OPTION_DEF
}

void ParseOptions(int argc, char *argv[])
{
    int cur_arg = 1;
    while (cur_arg < argc)
    {
        if (!strcmp("--help", argv[cur_arg]))
        {
            printf("Sim options:\n");
            PrintHelp();
            exit(0);
        }
#define OPTION_DEF(var, type, def, opt_name, arg_name, desc) \
    else if (var.ParseOption(cur_arg, argc, argv)) {}
#include "options_list.h"
#undef OPTION_DEF
        else
        {
            throw std::invalid_argument(std::string("Unrecognized argument: ") +
                                        argv[cur_arg]);
        }
    }
}

}   // namespace options
