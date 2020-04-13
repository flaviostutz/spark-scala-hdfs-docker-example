#!/bin/bash

if [ "$HDFS_URL" == "" ]; then
    echo "HDFS_URL is required"
    exit 1
fi

/submit.sh
