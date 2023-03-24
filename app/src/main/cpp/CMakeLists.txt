cmake_minimum_required(VERSION 3.22.1)

add_subdirectory(fdk-aac)

project("jni")
find_library(log-lib log)
aux_source_directory(. SRC_LIST)
add_library(jni SHARED ${SRC_LIST})
target_link_libraries(jni fdk-aac ${log-lib})
