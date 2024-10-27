library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity full_adder_tb is
end full_adder_tb;

architecture tb of full_adder_tb is

component full_adder is
    Port ( x : in STD_LOGIC;
           y : in STD_LOGIC;
           c_in : in STD_LOGIC;
           s : out STD_LOGIC;
           c_out : out STD_LOGIC);
end component;

signal x,y,c_in,s,c_out : std_logic := '0';
constant T : time := 10ns;

begin

    stimuli : process
    begin
        x <= '1';
        wait for T;
        c_in <= '1';
        wait for T;
        y <= '1';
        wait for T;
        c_in <= '0';
        wait for T;
    end process stimuli;
    
    mapa : full_adder port map(
        x => x,
        y => y,
        s => s,
        c_in => c_in,
        c_out => c_out
    );

end tb;
