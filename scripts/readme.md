# Notes about this folder

Hi, there. I'm Kevin, the lead mentor for 16750/20403, and I'm a retired
professional software developer. There are a couple scripts in here, of various
levels of value.

# Scripts

There are some scripts you can invoke from the package.json file that are useful
for doing some stuff.

## Using the scripts

In order to use the scripts, you'll first need to have the [Bun](https://bun.sh)
script runtime installed. Once that's installed, just like any of the Javascript
runtimes, you'll need to open a command prompt to the root of the repository,
then type `bun install`. From there, to use any of these scripts, you go to that
same directory and type `bun` _scriptname_, for example `bun format` will format
all the files the current student is editing.

### `format`

This runs the [Prettier](https://prettier.io) _opinionated_ code formatter on
any files that are being edited (not committed). I expect to integrate this into
a `git` pre-commit action at some point.

## Build scripts

### `full`

### `build` (must type `bun run build` not `bun build`)

A full build of everything (at least everything currently enabled). This
_should_ be the same as the "Build->Make Project" menu item in IntelliJ, and
whatever the VSCode WPILib task to build is. I should go find it.

# Other stuff maybe worth adding

- Launching WPILib VSCode to begin a coding session?
- Configuration stuff:
  - Setup a .gitconfig file with all my silly aliases
  - Anything else worth doing?
- If it's a 'stand-alone' app, here are some things that would be cool
  - Cloning the repo?
  - _Finding_ the repo
  - Installing git, bun, WPILib, etc...
