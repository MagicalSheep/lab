cmake_minimum_required(VERSION 3.0.0)
project(httpserver VERSION 0.1.0)

include(CTest)
enable_testing()

# set directories of .h files
include_directories(./include)

aux_source_directory(./src DIR_SRCS)

add_executable(httpserver main.c ${DIR_SRCS})
target_link_libraries(httpserver pthread)

set(CPACK_PROJECT_NAME ${PROJECT_NAME})
set(CPACK_PROJECT_VERSION ${PROJECT_VERSION})
include(CPack)
