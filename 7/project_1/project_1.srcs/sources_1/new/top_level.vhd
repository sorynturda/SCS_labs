library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity top_level is
    Port ( aclk : in std_logic;
           a : in STD_LOGIC_VECTOR (31 downto 0);
           min : in STD_LOGIC_VECTOR (31 downto 0);
           max : in STD_LOGIC_VECTOR (31 downto 0);
           sum : out STD_LOGIC_VECTOR (31 downto 0));
end top_level;

architecture Behavioral of top_level is

component saturator is
    Port (
        aclk : IN STD_LOGIC;
        s_axis_val_tvalid : IN STD_LOGIC;
        s_axis_val_tready : OUT STD_LOGIC;
        s_axis_val_tdata : IN STD_LOGIC_VECTOR(31 DOWNTO 0);
        s_axis_max_tvalid : IN STD_LOGIC;
        s_axis_max_tready : OUT STD_LOGIC;
        s_axis_max_tdata : IN STD_LOGIC_VECTOR(31 DOWNTO 0);
        s_axis_min_tvalid : IN STD_LOGIC;
        s_axis_min_tready : OUT STD_LOGIC;
        s_axis_min_tdata : IN STD_LOGIC_VECTOR(31 DOWNTO 0);
        m_axis_result_tvalid : OUT STD_LOGIC;
        m_axis_result_tready : IN STD_LOGIC;
        m_axis_result_tdata : OUT STD_LOGIC_VECTOR(31 DOWNTO 0)
        );
end component;

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

signal s_axis_min_tvalid, s_axis_max_tvalid, s_axis_val_tvalid : STD_LOGIC := '1';

signal m_axis_result_tvalid: STD_LOGIC := '0';
signal sat_data, swr_data : STD_LOGIC_VECTOR(31 DOWNTO 0) := (others => '0');


begin

    saturator_map : saturator port map(
        aclk,
        s_axis_val_tvalid, open,
        a, s_axis_max_tvalid, open, max,
        s_axis_min_tvalid, open, min,
        m_axis_result_tvalid, '1', sat_data
    );

    swa_map : sliding_window_adder port map(
        aclk,
        m_axis_result_tvalid, open, sat_data,
        open, '1', swr_data
    );
    
    sum <= swr_data;
end Behavioral;
