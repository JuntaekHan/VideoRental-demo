# VideoRental-demo

* **Constraints and Invariants**
    - Video store stocks only a single video for each title.
    - Only customers of legal age can rent videos.
    - Video return datetime cannot precede the rental datetime.


* The *rental charge* is calculated based on `PriceCode` and rental days.


* The *bonus point* is calculated based on `PriceCode` and overdue days.


* The *maximum allowable rental days without penalty* is dependent on the `VideoType`.
    - Video.VHS = 5
    - Video.CD = 3
    - Video.DVD = 2


* The *late return penalty* is dependent on the `VideoType`.
    - Video.VHS = 1
    - Video.CD = 2
    - Video.DVD = 3
    
   
* Immediate Changes
    - UI may be changed to Graphic UI and/or Web-based UI.
    - The `PriceCode` of each video may be changed during the video's lifetime.


* Future Changes
    - Customer report generation in other formats such as HTML may be added.
    - New `VideoType` maybe introduced. (`MediaType` seems to be a better naming than `VideoType`.)
    - Each video title may have multiple copies of different media types.
    - ...
