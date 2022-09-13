# TokyLib
A set of server-side APIs and libraries for easy and fancy creation of additional minecraft content with plugins. Integrated in [TokyPaper](https://github.com/maestro-denery/TokyPaper).
## Concepts / Components of this library.

### Content-addons
This is an IO / (De-)serialization system which makes it easier to make "additions" to a game content. The system may look like an ORM, but it doesn't necessarily interacts with a DB (Although it has an ability to...). \
Content-addons should be used when a game content doesn't have an ability to "carry" and save an additional data with it, as an example: Minecraft blocks, which don't have NBT or other containers where you can store an additional content to, unlike entities. \
This library is based on annotations, but also it heavily uses Mojang's serialization system, (Codec, DynamicOps, etc.) and has some concepts borrowed from Minecraft's Registry System.

## Contributing
All contributions are welcome! Feel free to make the library easier to use / faster / better documented / etc.

## License
`DRF` is released under the terms of the [MPL-2.0 license](LICENSE).
