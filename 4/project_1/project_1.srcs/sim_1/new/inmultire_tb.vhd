library IEEE;
use IEEE.STD_LOGIC_1164.ALL;


entity inmultire_tb is
end inmultire_tb;

architecture tb of inmultire_tb is

component inmultire is
    Port ( x : in STD_LOGIC_VECTOR (3 downto 0);
           y : in STD_LOGIC_VECTOR (3 downto 0);
           i : out STD_LOGIC_VECTOR (7 downto 0));
end component;

constant T : time := 10ns;
signal x,y : STD_LOGIC_VECTOR (3 downto 0) := (others => '0');
signal i : STD_LOGIC_VECTOR (7 downto 0) := (others => '0');

begin
    stimuli : process
    begin
        wait for T;
        x <= "1111";
        wait for T;
        y <= "0001";
        wait for T;
        y <= "0010";
        wait for T;
        wait;
    end process stimuli;
mapd : inmultire port map(x,y,i);
end tb;
