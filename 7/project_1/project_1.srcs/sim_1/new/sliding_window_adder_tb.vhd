library IEEE;
use IEEE.STD_LOGIC_1164.ALL;


entity sliding_window_adder_tb is
end sliding_window_adder_tb;

architecture tb of sliding_window_adder_tb is

component sliding_window_adder is
    Generic (
         WINDOW_SIZE : integer := 5
    );
    Port (
        aclk : IN STD_LOGIC;
        s_axis_val_tvalid : IN STD_LOGIC;
        s_axis_val_tready : OUT STD_LOGIC;
        s_axis_val_tdata : IN STD_LOGIC_VECTOR(31 DOWNTO 0);
        m_axis_sum_tvalid : OUT STD_LOGIC;
        m_axis_sum_tready : IN STD_LOGIC;
        m_axis_sum_tdata : OUT STD_LOGIC_VECTOR(31 DOWNTO 0)
    );
end component;

constant T : time := 10ns;
signal aclk, s_axis_val_tvalid, s_axis_val_tready, m_axis_sum_tvalid, m_axis_sum_tready: std_logic := '0';
signal m_axis_sum_tdata, s_axis_val_tdata : std_logic_vector (31 downto 0) := (others => '0');

begin

    clock_generator : process
    begin
        aclk <= '0';
        wait for T / 2;
        aclk <= '1';
        wait for T / 2;
    end process clock_generator;

    stimuli : process
    begin
        s_axis_val_tdata <= x"00000001";
        wait for T;
        s_axis_val_tvalid <= '1';
        m_axis_sum_tready <= '1';   
        wait for T;
        s_axis_val_tvalid <= '0';
        m_axis_sum_tready <= '0';
        wait for T;
        s_axis_val_tvalid <= '1';
        m_axis_sum_tready <= '1';
        wait for T;
        s_axis_val_tdata <= x"00000003";
        wait for T;
        s_axis_val_tvalid <= '0';
        m_axis_sum_tready <= '0';
        wait for T;
        s_axis_val_tvalid <= '1';
        m_axis_sum_tready <= '1';
        wait for T;
        s_axis_val_tdata <= x"00000006";
        wait for T;
        s_axis_val_tvalid <= '0';
        m_axis_sum_tready <= '0';
        wait for T;
    end process stimuli;

    swa : sliding_window_adder port map(aclk, s_axis_val_tvalid, s_axis_val_tready, s_axis_val_tdata,
        m_axis_sum_tvalid, m_axis_sum_tready, m_axis_sum_tdata); 
end tb;
