package fr.hyriode.hyriapi.impl.rank;

import fr.hyriode.hyriapi.rank.HyriRank;
import fr.hyriode.hyriapi.settings.HyriLanguage;

import java.util.ArrayList;
import java.util.Map;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class DefaultHyriRank extends HyriRank {

    public DefaultHyriRank(String name, Map<HyriLanguage, String> displayNames) {
        super(name, displayNames, new ArrayList<>());
    }

}

