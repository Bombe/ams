# AMS – Audio Management System

Quite the name for something that doesn’t even play MP3s.

## What Is It?

The aim for AMS is to support, in a painless and command-line based way:

* Displaying tags of (pretty much) arbitrary audio files.
* Modifying tags of said files.
* Convert files from one audio format to another.
* Renaming files based on the tag.

In some possible futures it might also get a GUI that makes using it even More Easier™.

## What Does It Already Do?

AMS already supports everything listed above. However, there are some restrictions one needs to be aware of:

* Loading files with wildcards requires a path to be specified, at least a relative one. Relying on not specifying the current directory does not work.
* Converting does not use a temporary path for the new files, it tries to put them into the same directory as the source files. This will fail if the source files are on a read-only medium, such as a CD-ROM.
* Converting is limited to conversion from WAV or FLAC to MP3.
* Quality parameters for the format conversion are not specifiable by parameters to the `convert` command.
* Error handling is limited to printing the name of the exception that occured and leaving the session in a probably working but undefined state.
* Converting, renaming, and saving files can only be applied to all files in the session.
* Saving tags currently only works for MP3 files, only as ID3v2 version 2.3 tags, and only as long as there are no previous tags in the files.

## How Do I Use It?

> $ java -jar ams.jar

This will start a session in the current directory.

> \> load /path/to/files

This will load all supported media files from the given directory.

> \> list

This lists all the files and information from their tags.

> \> album The Blue Horn

This will set the album on all files in the session.

> \> artist -t 1-4 The Random Lemmas

This will set artist information on tracks 1 thru 4. The rest of the files are not modified.

> \> track auto

This will set the track numbers on all files in the session, starting with 1 and increasing for each file, in the order they have been loaded into the session. It will also set the number of total tracks to the number of files in the session.

This command can also be combined with the `-t` (or `--tracks`) option to allow auto-numbering different sets of files, e.g. independent listings on different discs of a set.

> \> disc auto

This will automatically assing disc and total discs information to all files in the session. It will use the track information to find out how many different discs are defined in this session by scanning the files sequentially and starting a new disc every time the track of a file is a smaller number than the previous file’s track. In combination with `track -t 1-x auto` and `track -t x- auto` this can quickly be used to automatically assign track and disc numbers to all files of a larger set.

> \> convert

Converts all files to MP3. (Currently requires that source files are either FLAC and named accordingly, i.e. ending in “.flac” or WAV files.)

The original files are replaced by the newly produced files but the previously assigned tags are kept.

> \> rename ${Track%02d}\_${Artist}\_${Name}.mp3

Renames all files in the session according to the pattern.

> \> save

Saves all tag information to the files.