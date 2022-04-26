#! /bin/bash

# default downloading url
FOX_MOCK_FILE_URL="http://file.cxytiandi.com/foxmock.zip"

# exit shell with err_code
# $1 : err_code
# $2 : err_msg
exit_on_err()
{
    [[ ! -z "${2}" ]] && echo "${2}" 1>&2
    exit ${1}
}

# check permission to download && install
[ ! -w ./ ] && exit_on_err 1 "permission denied, target directory ./ was not writable."

if [ $# -gt 1 ] && [ $1 = "--url" ]; then
  shift
  FOX_MOCK_FILE_URL=$1
  shift
fi

# download from cxytiandi
echo "downloading... ${FOX_MOCK_FILE_URL}"
curl \
    -sLk \
    --connect-timeout 5000 \
    $FOX_MOCK_FILE_URL \
    -o "foxmock.zip" \
|| exit_on_err 1 "download failed!"


unzip "foxmock.zip"
chmod +x "foxmock/start.sh"

# done
echo "foxmock install successed."
