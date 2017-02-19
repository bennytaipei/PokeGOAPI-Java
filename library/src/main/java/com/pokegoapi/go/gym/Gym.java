package com.pokegoapi.go.gym;

import POGOProtos.Data.Gym.GymMembershipOuterClass.GymMembership;
import POGOProtos.Data.Gym.GymStateOuterClass.GymState;
import POGOProtos.Data.PokemonDataOuterClass.PokemonData;
import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import POGOProtos.Enums.TeamColorOuterClass.TeamColor;
import POGOProtos.Map.Fort.FortDataOuterClass.FortData;
import POGOProtos.Map.Fort.FortTypeOuterClass.FortType;
import POGOProtos.Networking.Responses.FortDeployPokemonResponseOuterClass.FortDeployPokemonResponse.Result;
import POGOProtos.Networking.Responses.GetGymDetailsResponseOuterClass.GetGymDetailsResponse;
import com.google.protobuf.ProtocolStringList;
import com.pokegoapi.GetInstance;
import com.pokegoapi.NoSuchTypeException;
import com.pokegoapi.Provider;
import com.pokegoapi.go.PokemonGoClient;
import com.pokegoapi.go.gym.spec.Battle;
import com.pokegoapi.go.map.spec.MapPoint;
import com.pokegoapi.network.LoginFailedException;
import com.pokegoapi.network.RemoteServerException;
import com.pokegoapi.old.api.pokemon.Pokemon;
import com.pokegoapi.old.exceptions.CaptchaActiveException;

import java.util.List;

@SuppressWarnings("unused")
public final class Gym implements MapPoint {

    private final GymSpi spi;
    private final Provider provider;

    private Gym(GymSpi spi, Provider provider){
        this.spi = spi;
        this.provider = provider;
    }

    public static Gym getInstance(){
        //TODO: Implement getting the default provider and get the instance from it
        return null;
    }

    public static Gym getInstance(Provider provider) throws NoSuchTypeException {
        GetInstance.Instance instance = GetInstance.getInstance("Gyms",
                GymSpi.class, provider);
        return new Gym((GymSpi) instance.impl, instance.provider);
    }

    public void initialize(PokemonGoClient client, FortData fort) {
        if(fort.getType() != FortType.GYM) {
            throw new IllegalArgumentException("Fort " + fort.getId() + " is not a Gym. " +
                    "Unable to initialize Gym object");
        }
        spi.engineInitialize(client, fort);
    }

    public void initialize(PokemonGoClient client, GetGymDetailsResponse details) {
        spi.engineInitialize(client, details);
    }

    public void initialize(PokemonGoClient client, GymState state) {
        spi.engineInitialize(client, state);
    }

    @Override
    public String getMapId() {
        return spi.getMapId();
    }

    @Override
    public double getLatitude() {
        return spi.getLatitude();
    }

    @Override
    public double getLongitude() {
        return spi.getLongitude();
    }

    public boolean isEnabled() {
        return spi.engineIsEnabled();
    }

    public TeamColor getTeamOwner() {
        return spi.engineGetOwnedByTeam();
    }

    public PokemonId getGuardPokemonId() {
        return spi.engineGetGuardPokemonId();
    }

    public int getGuardPokemonCp() {
        return spi.engineGetGuardPokemonCp();
    }

    public long getPoints() {
        return spi.engineGetPoints();
    }

    public boolean isInBattle() {
        return spi.engineIsInBattle();
    }

    public boolean isAttackable() {
        return spi.engineIsAttackable();
    }

    public String getName() {
        return spi.engineGetName();
    }

    public ProtocolStringList getUrlsList() {
        return spi.engineGetUrlsList();
    }

    public boolean isInRange() {
        return spi.engineInRange();
    }

    public String getDescription() {
        return spi.engineGetDescription();
    }

    public List<GymMembership> getGymMembers() {
        return spi.engineGetGymMembers();
    }

    /**
     * Get a list of pokemon defending this gym.
     *
     * @return List of pokemon
     */
    public List<PokemonData> getDefendingPokemon() {
        return spi.engineGetDefendingPokemon();
    }

    public GymState getGymState() {
        return spi.engineGetGymState();
    }

    public boolean hasGymDetails() {
        return spi.engineHasGymDetails();
    }

    public boolean hasGymState() {
        return spi.engineHasGymState();
    }

    public GetGymDetailsResponse getGymDetails() throws CaptchaActiveException, RemoteServerException, LoginFailedException {
        return spi.engineGetGymDetails();
    }

    public Result deployPokemon(Pokemon pokemon) throws CaptchaActiveException, RemoteServerException, LoginFailedException {
        return spi.engineDeployPokemon(pokemon);
    }

    public Battle battle() {
        return spi.engineBattleGym(this);
    }

    public Provider getProvider() {
        return provider;
    }
}