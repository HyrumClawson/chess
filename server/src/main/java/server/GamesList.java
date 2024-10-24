package server;

import model.ListingGameData;

import java.util.ArrayList;

public record GamesList(ArrayList<ListingGameData> games) {
}
