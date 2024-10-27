library IEEE;
use IEEE.STD_LOGIC_1164.ALL;


entity carry_lookahead_adder_tb is
end carry_lookahead_adder_tb;

architecture tb of carry_lookahead_adder_tb is

component carry_lookahead_adder is
    Port ( x : in STD_LOGIC_VECTOR (3 downto 0);
           y : in STD_LOGIC_VECTOR (3 downto 0);
           c_in : in STD_LOGIC;
           sum : out std_logic_vector (3 downto 0);
           c_out : out std_logic);
end component;

constant T : time := 10ns;
signal x,y,sum : STD_LOGIC_VECTOR (3 downto 0) := (others => '0');
signal c_in, c_out : std_logic := '0';
begin
    stimuli : process
    begin
        x <= "1000";
        wait for T;
        y <= "0010";
        wait for T;
        c_in <= '1';
        x <= "1111";
        wait for T;
        y <= "1111";
        wait for T;
        c_in <= '0';
        wait for T;
    end process stimuli;


    mapa : carry_lookahead_adder port map(x,y,c_in,sum,c_out);
end tb;
