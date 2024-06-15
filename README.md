# kotlin-plugin-template

![](https://img.shields.io/badge/MINECRAFT-1.16+-966C4A?style=for-the-badge&labelColor=53AC56)
![](https://img.shields.io/badge/JAVA-1.8+-5283A2?style=for-the-badge&labelColor=E86F00)

[Modrinth]() ·
[SpigotMC]() ·
[CurseForge]()

A Spigot (Bukkit) Minecraft plugin that...


## Features [WIP]

- [ ] Some useful stuff
- [ ] Another useful stuff


## Usage

### Commands

`/template` is the main plugin command, which has an alias `/tmp`.

| Command               | Description                                                   |
|-----------------------|---------------------------------------------------------------|
| `/tmp help [command]` | Show help for given command, for available commands otherwise |
| `/tmp reload`         | Reload config                                                 |


## Configuration ([default](/src/main/resources/config.yml))

- Plugin messages [WIP]
  - info
  - error
  - help


## Permissions

| Permission node   | Default | Description                                               |
|-------------------|---------|-----------------------------------------------------------|
| `template.help`   | true    | Allows to use `/tmp help` (lists only available commands) |
| `template.reload` | op      | Allows to use `/tmp reload`                               |
| `template.admin`  | op      | Refers to `template.reload` by default                    |


## Special thanks to:

- [Legitimoose](https://youtube.com/c/Legitimoose) for amazing Paper (Bukkit) plugin (in Kotlin) project setup [tutorial](https://youtu.be/5DBJcz0ceaw)
- [BeBr0](https://youtube.com/c/BeBr0) for Spigot (Bukkit) plugin development [tutorial [RU]](https://youtube.com/playlist?list=PLlLq-eYkh0bB_uyZN4NdzkxLBs9glZmIT) with very clear API explanation


## Copyright

The project itself is distributed under [GNU GPLv3](./LICENSE).
