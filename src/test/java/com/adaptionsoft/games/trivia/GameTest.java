package com.adaptionsoft.games.trivia;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.adaptionsoft.games.uglytrivia.Game;
import com.adaptionsoft.games.uglytrivia.GameOriginal;
import com.adaptionsoft.games.uglytrivia.IGame;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest
{

    @Test
    void goldenMasterTest()
    {
        for (int seed = 1; seed < 10_000; seed++)
        {
            String expectedOutput = extractOutput(new Random(seed), new Game());
            String actualOutput = extractOutput(new Random(seed), new GameOriginal());

            assertEquals(expectedOutput, actualOutput);
        }
    }

    private String extractOutput(Random rand, IGame game)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (PrintStream inMemory = new PrintStream(outputStream))
        {
            System.setOut(inMemory);

            game.add("Patryk");
            game.add("Piotr");
            game.add("Damian");
            game.add("Paweł");

            boolean notAWinner = false;
            do
            {
                game.roll(rand.nextInt(5) + 1);

                if (rand.nextInt(9) == 7)
                {
                    notAWinner = game.wrongAnswer();
                }
                else
                {
                    notAWinner = game.wasCorrectlyAnswered();
                }
            }
            while (notAWinner);

            return outputStream.toString();
        }
    }

}
