# Denery's Registry Framework
A set of server-side APIs and Libraries for interacting with Minecraft's Registry system. \
This frameworks consists of various libraries, APIs, tools

## base
Base is a common abstractions / libraries which are needed in many other subprojects, for now it includes event system (`net.drf.event`) very similar by a design with the Fabric one.

## registries
Registries are main abstractions describing Minecraft Registry system, abstractions are mainly similar to what minecraft have, but with better documentation and gurantees, it also includes registry lifecycle events and an ability for create own registries for the users of a framework.

## registry coverter
Registry converter is a converter of API to NMS regsitries and their entries, it uses SpongeVanilla for interaction with NMS in mojang mappings, and mainly inspired by the `io.papermc.paper.registry` internal classes in Paper and especially `PaperRegistry`.

## License
`DRF` is released under the terms of the [MPL-2.0 license](LICENSE).
