# 🏛️ AI Coliseum - Competitive AI Bot Arena

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](.) [![Java](https://img.shields.io/badge/Java-17+-blue)](.) [![License](https://img.shields.io/badge/license-MIT-green)](.)

## 📖 Overview

AI Coliseum is a competitive artificial intelligence platform where autonomous bots battle in strategic combat scenarios. Based on the **AIC2025 framework**, this repository hosts multiple AI bot implementations designed to compete in resource gathering, tactical combat, and territorial control.

## 🎯 Project Structure

```
AI_Coliseum/
├── AIC2025/                    # Main competition framework
│   ├── src/                    # Bot source code
│   │   ├── baibars/           # 🏆 Baibars Bot (Main project)
│   │   ├── demoplayer/        # Reference implementation
│   │   ├── meuli3/            # Alternative bot
│   │   ├── adolf/             # Experimental bot
│   │   └── nullplayer/        # Minimal bot
│   ├── maps/                  # Battle arenas
│   ├── games/                 # Match logs and replays
│   ├── jars/                  # Compiled libraries
│   └── build.xml              # Apache Ant build system
├── documentation/             # Project documentation
└── README.md                  # This file
```

## 🤖 Bot Implementations

### 🏆 **Baibars** - *Main Project*
> *"The Sultan of Strategic AI"*

**Inspired by:** Baibars I (1223-1277), legendary military strategist  
**Architecture:** Modular service-oriented design  
**Status:** ✅ Active Development  

**Key Features:**
- 🧠 **Tactical Intelligence**: Multi-phase strategic planning
- ⚔️ **Combat System**: Advanced weapon crafting and target prioritization  
- 🚶 **Smart Movement**: Anti-oscillation pathfinding with exploration
- 💰 **Resource Management**: Optimized collection for Total Weight victory
- 🕐 **Phase-Based Strategy**: Adaptive tactics for 2000-turn matches

**Services:**
- `BaibarsIntelligence.java` - Enemy detection and reconnaissance
- `BaibarsMovement.java` - Pathfinding and mobility
- `BaibarsCombat.java` - Weapon systems and combat
- `BaibarsTactics.java` - Strategic phase management
- `BaibarsResources.java` - Economic optimization
- `UnitPlayer.java` - Main controller

### 🎯 **Other Bots**
- **demoplayer**: Reference implementation and training opponent
- **meuli3**: Alternative strategic approach
- **adolf**: Experimental aggressive tactics  
- **nullplayer**: Minimal baseline implementation

## 🚀 Quick Start

### Prerequisites
- ☕ **Java 17+**
- 🐜 **Apache Ant** (for building)
- 🖥️ **Windows/Linux/macOS** compatible

### Installation

```bash
# Clone the repository
git clone https://github.com/gregoire-fortune/AI_Coliseum.git
cd AI_Coliseum/AIC2025

# Build the project
ant compile -Dpackage=baibars

# Run a match
ant run -Dpackage1=baibars -Dpackage2=demoplayer -Dmap=Basic1
```

### Available Maps
- `Basic1` - Simple terrain, ideal for testing
- `Basic2` - Moderate complexity with obstacles  
- `Basic3` - Advanced terrain features
- `testBed` - Large testing environment
- `horizontalBed` - Horizontal layout scenario
- `verticalBed` - Vertical layout scenario

## 🎮 Game Mechanics

### Victory Conditions
1. **Total Weight**: Accumulate maximum resources
2. **Bed Destruction**: Eliminate enemy spawn points
3. **Unit Elimination**: Destroy all enemy units

### Core Gameplay
- **Resource Gathering**: Collect materials for crafting and scoring
- **Weapon Crafting**: Create tools for combat and destruction
- **Tactical Movement**: Navigate terrain efficiently
- **Combat**: Engage enemies and destroy structures
- **Base Management**: Protect your beds while attacking enemy bases

## 🔧 Development

### Building Specific Bots

```bash
# Compile Baibars
ant compile -Dpackage=baibars

# Compile all bots
ant compile

# Run tournament
ant run -Dpackage1=baibars -Dpackage2=meuli3 -Dmap=testBed
```

### Adding New Bots

1. Create new package in `src/yourbot/`
2. Implement `UnitPlayer.java` with `run(UnitController uc)` method
3. Add to build system in `build.xml`
4. Test against existing bots

### Bot Development Guidelines

- 🚫 **No static variables** (AIC2025 instrumenter limitation)
- 📦 **Use modular architecture** for maintainability
- 🔄 **Implement anti-oscillation** for stable movement
- ⏱️ **Optimize for 2000-turn matches**
- 🧪 **Test against multiple opponents**

## 📊 Performance Metrics

### Baibars Bot Stats
- **Win Rate**: 60%+ against demoplayer
- **Primary Victory**: Total Weight accumulation
- **Compilation**: ✅ Zero warnings
- **Architecture**: 6 modular services
- **Anti-oscillation**: ✅ Implemented
- **Phase Strategy**: ✅ 2000-turn optimized

## 🎯 Competition Strategy

The AI Coliseum employs multiple strategic approaches:

1. **Economic Warfare**: Focus on resource accumulation
2. **Aggressive Tactics**: Direct combat and base destruction
3. **Hybrid Approach**: Balanced resource gathering and combat
4. **Defensive Strategy**: Base protection and counter-attacks

## 📈 Roadmap

- [ ] 🤖 **Advanced ML Integration**: Neural network decision making
- [ ] 🏆 **Tournament System**: Automated bracket competitions  
- [ ] 📊 **Analytics Dashboard**: Match statistics and performance metrics
- [ ] 🌐 **Web Interface**: Browser-based battle viewer
- [ ] 🔄 **Genetic Algorithms**: Evolutionary bot optimization

## 🤝 Contributing

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-bot`)
3. **Develop** your bot or improvements
4. **Test** thoroughly against existing bots
5. **Commit** your changes (`git commit -m 'Add amazing bot'`)
6. **Push** to the branch (`git push origin feature/amazing-bot`)
7. **Open** a Pull Request

### Contribution Guidelines
- 📝 Document your bot's strategy and architecture
- 🧪 Include test results against at least 2 existing bots
- 🎨 Follow existing code style and naming conventions
- 📊 Provide performance benchmarks

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🎖️ Credits

### Project Lead
- **Grégoire Fortune** - *Repository Owner & Baibars Architect*

### Historical Inspiration  
- **Baibars I** (1223-1277) - *Ayyubid Sultan and master military strategist*

### Framework
- **AIC2025** - *Competition platform and game engine*

## 📞 Contact

- 📧 **Email**: [contact@gregoire-fortune.com](mailto:contact@gregoire-fortune.com)
- 🐙 **GitHub**: [@gregoire-fortune](https://github.com/gregoire-fortune)
- 🌐 **Project Link**: [AI_Coliseum](https://github.com/gregoire-fortune/AI_Coliseum)

---

*"In the arena of artificial minds, only the most strategic shall triumph"* ⚔️

**Current Branch:** `Baibars` | **Last Updated:** September 29, 2025)