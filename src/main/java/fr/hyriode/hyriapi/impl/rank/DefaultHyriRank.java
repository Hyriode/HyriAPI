package fr.hyriode.hyriapi.impl.rank;

import fr.hyriode.hyriapi.rank.HyriRank;

import java.util.ArrayList;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class DefaultHyriRank extends HyriRank {

    public DefaultHyriRank(String name, String displayName, String description) {
        super(name, displayName, description, new ArrayList<>());
    }

}

