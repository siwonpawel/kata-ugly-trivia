package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/*
    players, places, purses, inPnealtyBox -> class Player
    all types of questions -> class QuestionBank ??



 */
public class Game implements IGame
{
    private List<String> players = new ArrayList<>();
    private int[] places = new int[6];
    private int[] purses = new int[6];
    private boolean[] inPenaltyBox = new boolean[6];

    private List<String> popQuestions = new LinkedList<>();
    private List<String> scienceQuestions = new LinkedList<>();
    private List<String> sportsQuestions = new LinkedList<>();
    private List<String> rockQuestions = new LinkedList<>();

    private int currentPlayer = 0;
    private boolean isGettingOutOfPenaltyBox;

    public Game()
    {
        for (int i = 0; i < 50; i++)
        {
            popQuestions.add("Pop Question " + i);
            scienceQuestions.add(("Science Question " + i));
            sportsQuestions.add(("Sports Question " + i));
            rockQuestions.add(createRockQuestion(i));
        }
    }

    public String createRockQuestion(int index)
    {
        return "Rock Question " + index;
    }

    @Override
    public boolean add(String playerName)
    {

        players.add(playerName);
        places[howManyPlayers()] = 0;
        purses[howManyPlayers()] = 0;
        inPenaltyBox[howManyPlayers()] = false;

        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
        return true;
    }

    public int howManyPlayers()
    {
        return players.size();
    }

    @Override
    public void roll(int roll)
    {
        System.out.println(players.get(currentPlayer) + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (inPenaltyBox[currentPlayer]) // TODO
        {
            if (roll % 2 != 0)
            {
                isGettingOutOfPenaltyBox = true;

                System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
                movePlayer(roll);
                System.out.println(players.get(currentPlayer)
                        + "'s new location is "
                        + getCurrentPlayer());
                System.out.println("The category is " + currentCategory().getLabel());
                askQuestion();
            }
            else
            {
                System.out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
            }

        }
        else
        {
            movePlayer(roll);
            System.out.println(players.get(currentPlayer)
                    + "'s new location is "
                    + getCurrentPlayer());
            System.out.println("The category is " + currentCategory().getLabel());
            askQuestion();
        }

    }

    private void movePlayer(int roll)
    {
        places[currentPlayer] = getCurrentPlayer() + roll;
        if (getCurrentPlayer() > 11)
            places[currentPlayer] = getCurrentPlayer() - 12;
    }

    private void askQuestion()
    {
        String question = switch (currentCategory())
                {
                    case POP -> popQuestions.remove(0);
                    case SCIENCE -> scienceQuestions.remove(0);
                    case SPORTS -> sportsQuestions.remove(0);
                    case ROCK -> rockQuestions.remove(0);
                };

        System.out.println(question);
    }

    enum Category
    {
        POP("Pop"),
        SPORTS("Sports"),
        SCIENCE("Science"),
        ROCK("Rock");

        private String label;

        Category(String label)
        {
            this.label = label;
        }

        public String getLabel()
        {
            return label;
        }
    }

    private Category currentCategory()
    {
        var mod = getCurrentPlayer() % 4;

        return switch (mod)
                {
                    case 0 -> Category.POP;
                    case 1 -> Category.SCIENCE;
                    case 2 -> Category.SPORTS;
                    default -> Category.ROCK;
                };

    }

    private int getCurrentPlayer()
    {
        return places[currentPlayer];
    }

	@Override
	public boolean wasCorrectlyAnswered() {
		if (inPenaltyBox[currentPlayer]){
			if (isGettingOutOfPenaltyBox) {
				System.out.println("Answer was correct!!!!");
				purses[currentPlayer]++;
				System.out.println(players.get(currentPlayer) 
						+ " now has "
						+ purses[currentPlayer]
						+ " Gold Coins.");
				
				boolean winner = didPlayerWin();
				currentPlayer++;
				if (currentPlayer == players.size()) currentPlayer = 0;
				
				return winner;
			} else {
				currentPlayer++;
				if (currentPlayer == players.size()) currentPlayer = 0;
				return true;
			}
			
			
			
		} else {
		
			System.out.println("Answer was correct!!!!");
			purses[currentPlayer]++;
			System.out.println(players.get(currentPlayer) 
					+ " now has "
					+ purses[currentPlayer]
					+ " Gold Coins.");
			
			boolean winner = didPlayerWin();
			currentPlayer++;
			if (currentPlayer == players.size()) currentPlayer = 0;
			
			return winner;
		}
	}
	
	@Override
	public boolean wrongAnswer(){
		System.out.println("Question was incorrectly answered");
		System.out.println(players.get(currentPlayer)+ " was sent to the penalty box");
		inPenaltyBox[currentPlayer] = true;
		
		currentPlayer++;
		if (currentPlayer == players.size()) currentPlayer = 0;
		return true;
	}

    private boolean didPlayerWin()
    {
        return !(purses[currentPlayer] == 6);
    }
}
