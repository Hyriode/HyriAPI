package fr.hyriode.hyriapi.impl.api.rank;

import fr.hyriode.hyriapi.rank.HyriRank;

import java.util.ArrayList;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 24/08/2021 at 18:42
 */
public class DefaultHyriRank extends HyriRank {

    public DefaultHyriRank(String name, String displayName, String description) {
        super(name, displayName, description, new ArrayList<>());
    }

}

