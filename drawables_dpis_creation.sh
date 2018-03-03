#!/bin/sh
# https://stackoverflow.com/a/25355105/5265908

DIR=$(dirname "${0}")
DIR_INPUT="${DIR}/drawables"
DIR_OUTPUT="${DIR}/src/main/res"

for I in ldpi mdpi hdpi xhdpi xxhdpi xxxhdpi; do
	mkdir -p "${DIR_OUTPUT}/drawable-${I}"
done

# ldpi | mdpi | hdpi | xhdpi | xxhdpi | xxxhdpi
# 0.75 | 1    | 1.5  | 2     | 3      | 4
# convert $1 -resize 75% drawable-ldpi/$1

for I in "${DIR_INPUT}"/*.png; do
	convert -verbose "${I}" -resize 36x36 "${DIR_OUTPUT}/drawable-ldpi/$(basename ${I})"
	convert -verbose "${I}" -resize 48x48 "${DIR_OUTPUT}/drawable-mdpi/$(basename ${I})"
	convert -verbose "${I}" -resize 72x72 "${DIR_OUTPUT}/drawable-hdpi/$(basename ${I})"
	convert -verbose "${I}" -resize 96x96 "${DIR_OUTPUT}/drawable-xhdpi/$(basename ${I})"
	convert -verbose "${I}" -resize 144x144 "${DIR_OUTPUT}/drawable-xxhdpi/$(basename ${I})"
	convert -verbose "${I}" -resize 192x192 "${DIR_OUTPUT}/drawable-xxxhdpi/$(basename ${I})"
done
