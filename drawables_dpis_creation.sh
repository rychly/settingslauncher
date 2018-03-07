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

for I in "${DIR_INPUT}"/*.svg; do
	FILENAME="$(basename ${I} .svg).png"
	convert -verbose -background none "${I}" -resize 36x36 "${DIR_OUTPUT}/drawable-ldpi/${FILENAME}"
	convert -verbose -background none "${I}" -resize 48x48 "${DIR_OUTPUT}/drawable-mdpi/${FILENAME}"
	convert -verbose -background none "${I}" -resize 72x72 "${DIR_OUTPUT}/drawable-hdpi/${FILENAME}"
	convert -verbose -background none "${I}" -resize 96x96 "${DIR_OUTPUT}/drawable-xhdpi/${FILENAME}"
	convert -verbose -background none "${I}" -resize 144x144 "${DIR_OUTPUT}/drawable-xxhdpi/${FILENAME}"
	convert -verbose -background none "${I}" -resize 192x192 "${DIR_OUTPUT}/drawable-xxxhdpi/${FILENAME}"
done
