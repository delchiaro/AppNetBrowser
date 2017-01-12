SRC_DIR=./
DST_DIR=./
SRC_DIR=./

protoc -I=$SRC_DIR --python_out=$DST_DIR $SRC_DIR/app.proto
#protoc -I=$SRC_DIR --python_out=$DST_DIR $SRC_DIR/temporary.proto

protoc -I=$SRC_DIR --java_out=$DST_DIR $SRC_DIR/app.proto
#protoc -I=$SRC_DIR --java_out=$DST_DIR $SRC_DIR/temporary.proto


